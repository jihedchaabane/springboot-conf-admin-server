package com.chj.gr.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chj.gr.entity.UserAccess;
import com.chj.gr.repository.UserAccessRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccessRepository userAccessRepository;

    public CustomUserDetailsService(UserAccessRepository userAccessRepository) {
        this.userAccessRepository = userAccessRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccess userAccess = userAccessRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!userAccess.isEnabled()) {
			throw new UsernameNotFoundException("User is disabled!");
		}
        if (!userAccess.isAccountNonExpired()) {
			throw new UsernameNotFoundException("User account is expired!");
		}
        if (!userAccess.isAccountNonLocked()) {
			throw new UsernameNotFoundException("User account is locked!");
		}
        if (!userAccess.isCredentialsNonExpired()) {
			throw new UsernameNotFoundException("User credentials are expired!");
		}
        
        return new org.springframework.security.core.userdetails.User(
        		userAccess.getUsername(),
        		userAccess.getPassword(),
        		userAccess.isEnabled(),
        		userAccess.isAccountNonExpired(),
        		userAccess.isCredentialsNonExpired(),
        		userAccess.isAccountNonLocked(),
        		/**
                userAccess.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()
                */
        		userAccess.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList()
        );
    }
}
