package com.restful.services;

import com.restful.controllers.UserController;
import com.restful.models.User;
import com.restful.models.UserInformation;
import com.restful.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    public void signup(User user) {
        if(existsByUsername(user.getUsername())) {
            // EmailAlreadyInUseException
            throw new RuntimeException("Email already in use");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // Used to get the authenticated user information
    public User getUser(String username) {
        return userRepository.findByUsername(username).get();
    }

    public UserInformation getUserInfo(String username) {
        if(!existsByUsername(username)) {
            throw new UsernameNotFoundException("This account don't exist");
        }

        User u = userRepository.findByUsername(username).get();
        if(u.isPrivateAccount()) {
            // PrivateAccountException: Private account
            throw new RuntimeException("Private account");
        }

        u.add(linkTo(methodOn(UserController.class).updateFirstName(null)).withSelfRel());
        u.add(linkTo(methodOn(UserController.class).updateLastName(null)).withSelfRel());
        u.add(linkTo(methodOn(UserController.class).updatePassword(null)).withSelfRel());
        u.add(linkTo(methodOn(UserController.class).updateAccountVisibility(false)).withSelfRel());

        userRepository.save(u);
        return u.convertToUserInformation();
    }

    // Method used in PostService
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public void updateAccountVisibility(boolean isPrivate, String username) {
        User u = userRepository.findByUsername(username).get();
        u.setPrivateAccount(isPrivate);
        userRepository.save(u);
    }

    public void updatePassword(String newPassword, String username) {
        User u = userRepository.findByUsername(username).get();
        u.setPassword(encoder.encode(newPassword));
        userRepository.save(u);
    }

    public void updateFirstName(String firstName, String username) {
        User u = userRepository.findByUsername(username).get();
        u.setFirstName(firstName);
        userRepository.save(u);
    }

    public void updateLastName(String lastName, String username) {
        User u = userRepository.findByUsername(username).get();
        u.setLastName(lastName);
        userRepository.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);

        if(opt.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }

        return new org.springframework.security.core.userdetails.User(
                opt.get().getUsername(), opt.get().getPassword(), opt.get().getAuthorities()
        );
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
