package myproject.springproject.Service;

import myproject.springproject.Entity.User;
import myproject.springproject.Entity.UserImage;
import myproject.springproject.Repository.UserImageRepository;
import myproject.springproject.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserImageService {
    private final UserImageRepository userImageRepository;
    private final UserRepository userRepository;

    public UserImageService(UserImageRepository userImageRepository, UserRepository userRepository) {
        this.userImageRepository = userImageRepository;
        this.userRepository = userRepository;
    }

    // Lưu ảnh vào database
    public UserImage saveUserImage(String userId, MultipartFile file) throws IOException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        UserImage image = new UserImage();
        image.setUser(user);
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setData(file.getBytes());

        return userImageRepository.save(image);
    }

    // Lấy danh sách ảnh theo userId
    public List<UserImage> getUserImages(String userId) {
        return userImageRepository.findByUserId(userId);
    }

    // Lấy ảnh theo imageId
    public UserImage getUserImageById(Long imageId) {
        return userImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    // Xóa ảnh theo imageId
    public void deleteUserImage(Long imageId) {
        if (!userImageRepository.existsById(imageId)) {
            throw new RuntimeException("Image not found");
        }
        userImageRepository.deleteById(imageId);
    }
}
