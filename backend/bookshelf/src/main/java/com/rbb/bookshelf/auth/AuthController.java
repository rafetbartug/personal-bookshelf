package com.rbb.bookshelf.auth;

import com.rbb.bookshelf.user.User;
import com.rbb.bookshelf.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public record LoginRequest(@NotBlank String usernameOrEmail, @NotBlank String password) {}
    public record LoginResponse(String token) {}


    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    // ctor’a ekle
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public record RegisterRequest(
            @NotBlank @Size(min = 3, max = 50) String username,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6, max = 100) String password
    ) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            return ResponseEntity.badRequest().body("username already taken");
        }
        if (userRepository.existsByEmail(req.email())) {
            return ResponseEntity.badRequest().body("email already taken");
        }

        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(User.Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("registered");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.usernameOrEmail(), req.password())
        );

        var principal = (UserDetails) auth.getPrincipal();
        String role = principal.getAuthorities().stream()
                .findFirst().map(a -> a.getAuthority()).orElse("ROLE_USER");
        String token = jwtService.generateToken(principal.getUsername(), role);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
