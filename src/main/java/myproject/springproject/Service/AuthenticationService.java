package myproject.springproject.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import myproject.springproject.DTO.Request.AuthenticationRequest;
import myproject.springproject.DTO.Request.IntrospectRequest;
import myproject.springproject.DTO.Response.AuthenticationResponse;
import myproject.springproject.DTO.Response.IntrospectResponse;
import myproject.springproject.Exception.AppException;
import myproject.springproject.Exception.ErrorCode;
import myproject.springproject.Repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    protected static final String SIGNER_KEY = "gpRMtQrumOEr2XrbJNlj6xMWcuJhQhG9sEqc9C+BDI+w65FQgMA6whS9SKWXdmJy";

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
            .valid(verified && expireTime.after(new Date()))
            .build(); 
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(request.getUsername());

        return AuthenticationResponse.builder()
            .token(token)
            .authenticated(true)
            .build();
    }

    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(username)
            .issuer("springproject.com")
            .issueTime(new Date())
            .expirationTime(new Date(Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()))
            .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e){
            throw new RuntimeException(e);
        }
        
    }
}
