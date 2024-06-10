package com.sparta.springnewsfeed.security;

import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("This method should not be used");
    }

    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with userId: " + userId));
        return UserPrincipal.create(user);
    }
}