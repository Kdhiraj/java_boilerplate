package com.eventline.api.v1.User.Service;

import com.eventline.api.v1.User.DTO.ProfileRecordDto;
import com.eventline.api.v1.User.model.User;
import com.eventline.shared.exceptions.ResourceNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    // Method to get the authenticated user
    @Override
    public User getAuthenticatedUser() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new ResourceNotFoundException("User not found");
        }
        return (User) authentication.getPrincipal();
    }

    // Method to map User to ProfileDto
    @Override
    public ProfileRecordDto mapToProfileDto(@NotNull User user) {
        return new ProfileRecordDto(
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getProfileImage(),
                user.getLoginType(),
                user.getCountryCode(),
                user.getPhoneNumber(),
                user.getRoles(),
                user.getLastLogin(),
                user.getCreatedAt(),
                user.getExpirationDate(),
                user.getLastPasswordChangeDate(),
                user.isEmailVerified(),
                user.isPhoneVerified(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isLocked(),
                user.isEnabled()
        );
    }
}
