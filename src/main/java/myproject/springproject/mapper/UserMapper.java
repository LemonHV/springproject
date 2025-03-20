package myproject.springproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import myproject.springproject.DTO.Request.UserCreationRequest;
import myproject.springproject.DTO.Request.UserUpdateRequest;
import myproject.springproject.DTO.Response.UserResponse;
import myproject.springproject.Entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
