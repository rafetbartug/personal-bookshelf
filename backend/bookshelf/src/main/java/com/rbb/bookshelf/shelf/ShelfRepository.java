package com.rbb.bookshelf.shelf;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    List<Shelf> findAllByUserId(Long userId);
    Optional<Shelf> findByIdAndUserId(Long id, Long userId);
}
