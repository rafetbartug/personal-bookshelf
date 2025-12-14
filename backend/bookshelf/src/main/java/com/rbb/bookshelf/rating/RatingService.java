package com.rbb.bookshelf.rating;

import com.rbb.bookshelf.book.Book;
import com.rbb.bookshelf.book.BookRepository;
import com.rbb.bookshelf.rating.dto.RatingResponse;
import com.rbb.bookshelf.rating.dto.RatingSummaryResponse;
import com.rbb.bookshelf.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;

    private RatingResponse toResponse(Rating r) {
        return RatingResponse.builder()
                .id(r.getId())
                .score(r.getScore())
                .comment(r.getComment())
                .ratedAt(r.getRatedAt())
                .bookId(r.getBook().getId())
                .bookTitle(r.getBook().getTitle())      // <-- YENİ
                .bookCoverUrl(r.getBook().getCoverUrl())// <-- YENİ
                .userId(r.getUser().getId())
                .username(r.getUser().getUsername())
                .build();
    }

    public RatingResponse rate(User user, Long bookId, int score, String comment) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı"));

        try {
            Rating r = Rating.builder()
                    .user(user)
                    .book(book)
                    .score(score)
                    .comment(comment)
                    .ratedAt(LocalDateTime.now())
                    .build();
            return toResponse(ratingRepository.save(r));
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Bu kitabı zaten oyladınız!");
        }
    }

    public List<RatingResponse> listForBook(Long bookId) {
        return ratingRepository.findAllByBookId(bookId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RatingResponse myRating(Long userId, Long bookId) {
        Rating r = ratingRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Henüz oylamadınız."));
        return toResponse(r);
    }

    public RatingResponse updateMyRating(Long userId, Long bookId, Integer score, String comment) {
        Rating r = ratingRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Oylama bulunamadı."));

        if (score != null) r.setScore(score);
        if (comment != null) r.setComment(comment);
        r.setRatedAt(LocalDateTime.now()); // Güncellenme tarihi

        return toResponse(ratingRepository.save(r));
    }

    public void deleteMyRating(Long userId, Long bookId) {
        Rating r = ratingRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Oylama bulunamadı."));
        ratingRepository.delete(r);
    }

    public RatingSummaryResponse summary(Long bookId) {
        List<Object[]> result = ratingRepository.summary(bookId);
        Object[] row = result.stream().findFirst().orElse(null);

        long count = 0L;
        Double avg = 0.0;

        if (row != null) {
            if (row[0] != null) count = ((Number) row[0]).longValue();
            if (row[1] != null) avg = ((Number) row[1]).doubleValue();
        }

        return RatingSummaryResponse.builder()
                .bookId(bookId)
                .count(count)
                .avg(avg)
                .build();
    }

    public List<RatingResponse> getMyAllRatings(Long userId) {
        return ratingRepository.findAllByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}