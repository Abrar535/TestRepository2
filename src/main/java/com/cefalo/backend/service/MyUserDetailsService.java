package com.cefalo.backend.service;

import com.cefalo.backend.model.User;
import com.cefalo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUserId(userId));
        if (!user.isPresent()) {
            String msg = String.format("UserAccount '%s' not found.", userId);
            throw new UsernameNotFoundException(msg);
        }
        // assign authorities to the user:
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("CRUD"));

        User userObtained = user.get();
        return new org.springframework.security.core.userdetails.User(userObtained.getUserId(), userObtained.getPassword(), authorities);
    }
}
