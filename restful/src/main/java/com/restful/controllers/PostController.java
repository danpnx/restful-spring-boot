package com.restful.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restful.models.Post;
import com.restful.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@Tag(name = "User", description = "Endpoints to access posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "Create a post", description = "Create a post", tags = {"Post"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content)
            }
    )
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody @Valid Post post) {
        String username = getUsername();
        postService.createPost(post, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get user posts", description = "Get all posts from the current user", tags = {"Post", "User"},
            responses = {
                    @ApiResponse(
                            description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Post.class))
                            )
                    ),
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<Post>> getUserPosts() {
        String username = getUsername();
        return ResponseEntity.ok(postService.getUserPosts(username));
    }

    @Operation(
            summary = "Get post from id", description = "Get post from id", tags = {"Post"},
            responses = {
                    @ApiResponse(
                            description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json", schema = @Schema(implementation = Post.class)
                            )
                    ),
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(postService.getPostById(UUID.fromString(id)));
    }

    @Operation(
            summary = "Delete post", description = "Delete post by id", tags = {"Post"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content),
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content),
                    @ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(value = "id") String id) {
        String username = getUsername();
        postService.deletePost(UUID.fromString(id), username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> likePost(@PathVariable(value = "id") String id) {
        String username = getUsername();
        postService.likePost(UUID.fromString(id), username);
        return ResponseEntity.ok().build();
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
