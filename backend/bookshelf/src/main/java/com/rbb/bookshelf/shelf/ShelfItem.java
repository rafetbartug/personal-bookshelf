package com.rbb.bookshelf.shelf;

import com.rbb.bookshelf.book.Book;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "shelf_items")
public class ShelfItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "added_at")
    private LocalDateTime addedAt; // DB'de nullable

    @Column(name = "progress_percent")
    private Integer progressPercent;

    @Enumerated(EnumType.STRING)
    private ReadingStatus status; // enum('FINISHED','PLANNED','READING')

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shelf_id", nullable = false)
    private Shelf shelf;

    @PrePersist
    void onAdd() {
        if (addedAt == null) addedAt = LocalDateTime.now();
    }
}
