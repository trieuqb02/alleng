package com.alleng.identity.payload.request;

public record UserVm(String username, String email, String newPassword, String oldPassword, String fullName) {
}
