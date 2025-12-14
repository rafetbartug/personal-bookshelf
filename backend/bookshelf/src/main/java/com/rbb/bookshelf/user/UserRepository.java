package com.rbb.bookshelf.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // YENİ: En çok yorum yapan kullanıcıyı bul (Rating tablosuyla join atar)
    @Query(value = "SELECT u.username FROM users u " +
            "JOIN ratings r ON u.id = r.user_id " +
            "GROUP BY u.username " +
            "ORDER BY COUNT(r.id) DESC LIMIT 1", nativeQuery = true)
    String findTopReviewer();
}