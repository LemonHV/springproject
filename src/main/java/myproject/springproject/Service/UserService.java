package myproject.springproject.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import myproject.springproject.DTO.Request.UserCreationRequest;
import myproject.springproject.DTO.Request.UserUpdateRequest;
import myproject.springproject.DTO.Response.UserResponse;
import myproject.springproject.Entity.User;
import myproject.springproject.Exception.AppException;
import myproject.springproject.Exception.ErrorCode;
import myproject.springproject.Repository.UserRepository;
import myproject.springproject.mapper.UserMapper;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public User createUser(UserCreationRequest request) {
        
        if (userRepository.existsByUsername(request.getUsername())) 
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);      
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);  
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
