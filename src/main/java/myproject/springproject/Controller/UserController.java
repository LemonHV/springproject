package myproject.springproject.Controller;

import java.util.List;
import myproject.springproject.SpringprojectApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import myproject.springproject.DTO.Request.UserCreationRequest;
import myproject.springproject.DTO.Request.UserUpdateRequest;
import myproject.springproject.DTO.Response.ApiResponse;
import myproject.springproject.DTO.Response.UserResponse;
import myproject.springproject.Entity.User;
import myproject.springproject.Service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final SpringprojectApplication springprojectApplication;
    @Autowired
    private UserService userService;

    UserController(SpringprojectApplication springprojectApplication) {
        this.springprojectApplication = springprojectApplication;
    }

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }

}
