package com.hackathon.team5.service;

import com.hackathon.team5.entity.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        CustomUser customUser = userService.findByEmail(email);




        String emal = customUser.getEmail();
        String password = customUser.getPassword();
        //      String role = customUser.getRole().toString();


        if (customUser.getEmail()==null)
            throw new UsernameNotFoundException(email + " not found");

        List<GrantedAuthority> roles =
                Arrays.asList(
                        new SimpleGrantedAuthority(customUser.getRole().toString()));

        return new User(customUser.getEmail(),
                customUser.getPassword(),roles);
    }
}
