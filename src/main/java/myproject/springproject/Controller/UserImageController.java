package myproject.springproject.Controller;

import myproject.springproject.Entity.UserImage;
import myproject.springproject.Service.UserImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user-images")
public class UserImageController {
    private final UserImageService userImageService;

    public UserImageController(UserImageService userImageService) {
        this.userImageService = userImageService;
    }

    // API upload ảnh cho User
    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadImage(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        try {
            userImageService.saveUserImage(userId, file);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }

    // API lấy danh sách ảnh của User
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserImage>> getUserImages(@PathVariable String userId) {
        List<UserImage> images = userImageService.getUserImages(userId);
        return ResponseEntity.ok(images);
    }

    // API xem ảnh theo imageId
    @GetMapping("/view/{imageId}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long imageId) {
        UserImage image = userImageService.getUserImageById(imageId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, image.getFileType())
                .body(image.getData());
    }

    // API xóa ảnh theo imageId
    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        try {
            userImageService.deleteUserImage(imageId);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error deleting image: " + e.getMessage());
        }
    }
}
