package com.eventline.api.v1.User.Service;

import com.eventline.api.v1.User.DTO.ProfileRecordDto;
import com.eventline.api.v1.User.model.User;
import com.eventline.shared.exceptions.ResourceNotFoundException;

public interface UserService {
    User getAuthenticatedUser() throws ResourceNotFoundException;

    ProfileRecordDto mapToProfileDto(User user);
}
