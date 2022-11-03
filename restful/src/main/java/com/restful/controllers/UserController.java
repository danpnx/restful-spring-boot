package com.restful.controllers;

import com.restful.models.User;
import com.restful.models.UserInformation;
import com.restful.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoint for access to user's data")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Find user info", description = "Get user information", tags = {"User"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )
                    ),
                    @ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content)
            }
    )
    @GetMapping("/profile")
    public ResponseEntity<User> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        return ResponseEntity.ok(userService.getUserInfo(username));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserInformation> getUserByUsername(@PathVariable(value = "username") String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping("/config/privacy")
    public ResponseEntity<?> updateAccountVisibility(@RequestParam boolean isPrivate) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userService.updateAccountVisibility(isPrivate, username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/config/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userService.updatePassword(newPassword, username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/config/update-first-name")
    public ResponseEntity<?> updateFirstName(@RequestParam String firstName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userService.updateFirstName(firstName, username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/config/update-last-name")
    public ResponseEntity<?> updateLastName(@RequestParam String lastName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userService.updateLastName(lastName, username);
        return ResponseEntity.ok().build();
    }
}
