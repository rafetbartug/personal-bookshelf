package com.rbb.bookshelf.common;

import com.rbb.bookshelf.user.User;
import com.rbb.bookshelf.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User requireUser(Authentication auth) {
        String name = auth.getName(); // username veya email

        return userRepository.findByUsername(name)
                .or(() -> userRepository.findByEmail(name))
                .orElseThrow(() ->
                        new com.rbb.bookshelf.common.NotFoundException(
                                "User not found for principal: " + name
                        )
                );
    }

    public Long requireUserId(Authentication auth) {
        return requireUser(auth).getId();
    }
}
