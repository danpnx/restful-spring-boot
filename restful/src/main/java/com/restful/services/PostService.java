package com.restful.services;

import com.restful.models.Post;
import com.restful.models.User;
import com.restful.models.UserInformation;
import com.restful.repositories.PostRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public void createPost(Post post, String username) {
        User u = userService.getUserByUsername(username);
        post.setUser(u);
        postRepository.save(post);
    }

    public List<Post> getUserPosts(String username) {
        UserInformation u = userService.getUserInfo(username);

        if(u.getPosts().isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }

        return u.getPosts();
    }

    public Post getPostById(UUID id) {
        Optional<Post> opt = postRepository.findById(id);

        if(opt.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }

        if(opt.get().getUser().isPrivateAccount()) {
            throw new RuntimeException("Private account");
        }

        return opt.get();
    }

    public void deletePost(UUID id, String username) {
        Optional<Post> opt = postRepository.findById(id);
        List<Post> posts = userService.getUser(username).getPosts();

        if(opt.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }

        if(!posts.contains(opt.get())) {
            throw new RuntimeException("You can't delete this post");
        }

        postRepository.deleteById(id);
    }

    public void likePost(UUID id, String username) {
        Optional<Post> opt = postRepository.findById(id);

        if(opt.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }

        // if the account related to the post is private and is not the same account as the actual user
        if(opt.get().getUser().isPrivateAccount() && !opt.get().getUser().getUsername().equals(username)) {
            throw new RuntimeException("Private account");
        }

        opt.get().setLikes(opt.get().getLikes() + 1);
        postRepository.save(opt.get());
    }
}
