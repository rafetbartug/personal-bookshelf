package com.rbb.bookshelf.shelf;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShelfItemRepository extends JpaRepository<ShelfItem, Long> {
    List<ShelfItem> findAllByShelfId(Long shelfId);
    Optional<ShelfItem> findByIdAndShelfId(Long id, Long shelfId);
    boolean existsByShelfIdAndBookId(Long shelfId, Long bookId);
    long deleteAllByShelfId(Long shelfId);
}
