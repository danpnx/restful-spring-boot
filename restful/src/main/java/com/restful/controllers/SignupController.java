package com.restful.controllers;

import com.restful.models.User;
import com.restful.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@Tag(name = "Signup", description = "Endpoint for signup")
public class SignupController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Sign up in the platform", description = "Sign up to get access to additional features",
            tags ={"Signup"},
            responses ={
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content),
                    @ApiResponse(description = "UNAUTHORIZED", responseCode = "401", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<?> signup(@RequestBody User user) {
        userService.signup(user);
        return ResponseEntity.ok().build();
    }
}
