package com.alleng.identity.payload.response;

import com.alleng.identity.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserMV(UUID userId, String username, String thumbnail, String mail, String fullName , List<RoleMV> list) {
    public static UserMV convertUserMV(User user){
        List<RoleMV> roles = user.getRoles().stream().map(RoleMV::convertRoleMV).toList();
        return new UserMV(user.getId(), user.getUsername(), user.getThumbnail(), user.getEmail(), user.getFullName(), roles);
    }
}
