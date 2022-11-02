package com.restful.services;

import com.restful.models.User;
import com.restful.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    // temporary
    public List<User> findAll() {
        if(userRepository.findAll().size() == 0) {
            throw new EmptyResultDataAccessException("Any user found in database", 1);
        }

        return userRepository.findAll();
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
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
}
