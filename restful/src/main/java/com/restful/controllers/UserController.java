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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoint to access user's data")
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
        String username = getUsername();

        return ResponseEntity.ok(userService.getUser(username));
    }

    @Operation(
            summary = "Find user by username", description = "Find user by username", tags = {"User"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    ),
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content)
            }
    )
    @GetMapping("/{username}")
    public ResponseEntity<UserInformation> getUserByUsername(@PathVariable(value = "username") String username) {
        return ResponseEntity.ok(userService.getUserInfo(username));
    }

    @Operation(
            summary = "Change account privacy", description = "Changes if the account is visible for other users",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content)
            }
    )
    @PutMapping("/config/privacy")
    public ResponseEntity<?> updateAccountVisibility(@RequestParam boolean isPrivate) {
        String username = getUsername();
        userService.updateAccountVisibility(isPrivate, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update user password", description = "Update user password", tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "OK", responseCode = "200", content = @Content
                    )
            }
    )
    @PutMapping("/config/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam String newPassword) {
        String username = getUsername();
        userService.updatePassword(newPassword, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update user first name", description = "Update user first name", tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "OK", responseCode = "200", content = @Content
                    )
            }
    )
    @PutMapping("/config/update-first-name")
    public ResponseEntity<?> updateFirstName(@RequestParam String firstName) {
        String username = getUsername();
        userService.updateFirstName(firstName, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update user last name", description = "Update user last name", tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "OK", responseCode = "200", content = @Content
                    )
            }
    )
    @PutMapping("/config/update-last-name")
    public ResponseEntity<?> updateLastName(@RequestParam String lastName) {
        String username = getUsername();
        userService.updateLastName(lastName, username);
        return ResponseEntity.ok().build();
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
