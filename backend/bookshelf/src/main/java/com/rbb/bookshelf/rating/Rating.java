package com.rbb.bookshelf.rating;

import com.rbb.bookshelf.book.Book;
import com.rbb.bookshelf.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings",
        uniqueConstraints = @UniqueConstraint(name = "uq_ratings_user_book", columnNames = {"user_id", "book_id"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int score; // 0..10 validation DTO'da

    @Lob
    private String comment;

    @Column(name = "rated_at")
    private LocalDateTime ratedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    void prePersist() {
        if (ratedAt == null) ratedAt = LocalDateTime.now();
    }
}
