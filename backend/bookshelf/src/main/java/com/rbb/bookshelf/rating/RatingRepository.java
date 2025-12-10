package com.rbb.bookshelf.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    Optional<Rating> findByUserIdAndBookId(Long userId, Long bookId);
    List<Rating> findAllByBookId(Long bookId);
    Optional<Rating> findByIdAndBookId(Long id, Long bookId);
    @Query(value = "select count(*) as cnt, avg(score) as avg_score from ratings where book_id = :bookId", nativeQuery = true)
    List<Object[]> summary(@Param("bookId") Long bookId);

}
