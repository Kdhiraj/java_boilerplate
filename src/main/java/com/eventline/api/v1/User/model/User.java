package com.eventline.api.v1.User.model;

import com.eventline.shared.constants.AppEnums;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private ObjectId id;
    private String fullName;

    @Indexed
    private String email;
    private boolean isEmailVerified = false;

    @JsonIgnore
    private String password;
    private String profileImage;

    @Indexed
    private boolean isEnabled = true; // Account is enabled or disabled
    private boolean isLocked = false; // Account is locked
    @Indexed
    private boolean isDeleted = false; // Account is deleted (soft delete)
    private String loginType;
    private String countryCode;
    private String phoneNumber;
    private boolean isPhoneVerified = false;
    private List<AppEnums.Role> roles = new ArrayList<>();
    private Instant lastLogin; // Changed to Instant

    @CreatedDate
    private Instant createdAt; // Changed to Instant

    @LastModifiedDate
    private Instant updatedAt; // Changed to Instant

    private Instant expirationDate; // Changed to Instant
    private Instant lastPasswordChangeDate; // Changed to Instant

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (AppEnums.Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id.toHexString();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Check if the account is expired
        return expirationDate == null || expirationDate.isAfter(Instant.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        // Return true if the account is not locked
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Assuming passwords expire after a set period, e.g., 90 days
        return lastPasswordChangeDate == null ||
                lastPasswordChangeDate.isAfter(Objects.requireNonNull(getPasswordExpirationDate()));
    }

    // A method to calculate the password expiration date (e.g., 90 days after last change)
    private Instant getPasswordExpirationDate() {
        if (lastPasswordChangeDate != null) {
            return lastPasswordChangeDate.plusMillis(AppEnums.TTL.PWD_EXPIRATION);
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
