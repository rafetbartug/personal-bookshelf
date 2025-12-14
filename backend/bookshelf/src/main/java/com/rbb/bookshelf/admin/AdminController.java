package com.rbb.bookshelf.admin;

import com.rbb.bookshelf.book.BookRepository;
import com.rbb.bookshelf.rating.RatingRepository;
import com.rbb.bookshelf.rating.dto.RatingResponse;
import com.rbb.bookshelf.shelf.ShelfRepository;
import com.rbb.bookshelf.shelf.dto.ShelfResponse;
import com.rbb.bookshelf.user.User;
import com.rbb.bookshelf.user.UserRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final ShelfRepository shelfRepository;

    // 1. İstatistikler (Zaten vardı)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public DashboardStats getStats() {
        return DashboardStats.builder()
                .totalUsers(userRepository.count())
                .totalBooks(bookRepository.count())
                .totalRatings(ratingRepository.count())
                .totalShelves(shelfRepository.count())
                .topUser(userRepository.findTopReviewer())
                .build();
    }

    // 2. Tüm Kullanıcıları Getir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> UserDto.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .email(u.getEmail())
                        .role(u.getRole().name())
                        .build())
                .collect(Collectors.toList());
    }

    // Kullanıcı Silme
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    // 3. Tüm Yorumları Getir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reviews")
    public List<RatingResponse> getAllReviews() {
        return ratingRepository.findAll().stream()
                .map(r -> RatingResponse.builder()
                        .id(r.getId())
                        .score(r.getScore())
                        .comment(r.getComment())
                        .ratedAt(r.getRatedAt())
                        .bookId(r.getBook().getId())
                        .bookTitle(r.getBook().getTitle())
                        .bookCoverUrl(r.getBook().getCoverUrl())
                        .userId(r.getUser().getId())
                        .username(r.getUser().getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@PathVariable Long id) {
        ratingRepository.deleteById(id);
    }

    // 4. Tüm Rafları Getir (Kimin rafı olduğunu da ekleyerek)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/shelves")
    public List<ShelfResponse> getAllShelves() {
        return shelfRepository.findAll().stream()
                .map(s -> ShelfResponse.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .isPublic(s.isPublic())
                        .name(s.getName() + " (Sahibi: " + s.getUser().getUsername() + ")")
                        .build())
                .collect(Collectors.toList());
    }

    // Basit User DTO (Şifreleri göndermemek için)
    @Data @Builder
    public static class UserDto {
        private Long id;
        private String username;
        private String email;
        private String role;
    }
}