package com.rbb.bookshelf.api;

import com.rbb.bookshelf.user.User;
import com.rbb.bookshelf.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserRepository repo;

    public UserApiController(UserRepository repo) {
        this.repo = repo;
    }

    public record MeResponse(Long id, String username, String email, String role) {}

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        String username = auth.getName();
        User u = repo.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(new MeResponse(u.getId(), u.getUsername(), u.getEmail(), "ROLE_" + u.getRole().name()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/ping")
    public String adminPing() {
        return "admin-pong";
    }
}
