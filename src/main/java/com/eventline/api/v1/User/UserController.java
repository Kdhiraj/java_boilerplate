package com.eventline.api.v1.User;

import com.eventline.api.v1.User.DTO.ProfileRecordDto;
import com.eventline.api.v1.User.Service.UserService;
import com.eventline.api.v1.User.model.User;
import com.eventline.shared.constants.Messages;
import com.eventline.shared.exceptions.ResourceNotFoundException;
import com.eventline.shared.response.ApiSuccessResponse;
import com.eventline.shared.response.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
@SecurityRequirement(name = "jwtAuth")
@Tag(name = "User Module", description = "API related to user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiSuccessResponse<ProfileRecordDto>> authenticatedUser() throws ResourceNotFoundException {
        User currentUser = userService.getAuthenticatedUser();
        ProfileRecordDto response = userService.mapToProfileDto(currentUser);
        return ResponseUtil.buildSuccessResponse(Messages.SUCCESS, response);
    }
}
