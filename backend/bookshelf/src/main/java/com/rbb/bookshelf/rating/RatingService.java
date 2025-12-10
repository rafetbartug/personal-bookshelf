package com.rbb.bookshelf.rating;

import com.rbb.bookshelf.book.Book;
import com.rbb.bookshelf.book.BookRepository;
import com.rbb.bookshelf.common.NotFoundException;
import com.rbb.bookshelf.rating.dto.RatingResponse;
import com.rbb.bookshelf.rating.dto.RatingSummaryResponse;
import com.rbb.bookshelf.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .userId(r.getUser().getId())
                .username(r.getUser().getUsername())
                .build();
    }

    public RatingResponse rate(User user, Long bookId, int score, String comment) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        try {
            Rating r = Rating.builder()
                    .user(user)
                    .book(book)
                    .score(score)
                    .comment(comment)
                    .build();
            return toResponse(ratingRepository.save(r));
        } catch (DataIntegrityViolationException e) {
            // uq_ratings_user_book (duplicate)
            throw new IllegalArgumentException("You already rated this book");
        }
    }

    public List<RatingResponse> listForBook(Long bookId) {
        return ratingRepository.findAllByBookId(bookId).stream().map(this::toResponse).toList();
    }

    public RatingResponse myRating(Long userId, Long bookId) {
        Rating r = ratingRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new NotFoundException("Rating not found"));
        return toResponse(r);
    }

    public RatingResponse updateMyRating(Long userId, Long bookId, Integer score, String comment) {
        Rating r = ratingRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new NotFoundException("Rating not found"));

        if (score != null) r.setScore(score);
        if (comment != null) r.setComment(comment);

        return toResponse(ratingRepository.save(r));
    }

    public void deleteMyRating(Long userId, Long bookId) {
        Rating r = ratingRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new NotFoundException("Rating not found"));
        ratingRepository.delete(r);
    }

    public RatingSummaryResponse summary(Long bookId) {
        Object[] row = ratingRepository.summary(bookId).stream().findFirst().orElse(null);

        // native query bazen nested array doner: [ [cnt, avg] ]
        if (row != null && row.length == 1 && row[0] instanceof Object[]) {
            row = (Object[]) row[0];
        }

        long count = 0L;
        Double avg = null;

        if (row != null && row.length >= 2) {
            if (row[0] != null) count = ((Number) row[0]).longValue();
            if (row[1] != null) avg = ((Number) row[1]).doubleValue();
        }

        return RatingSummaryResponse.builder()
                .bookId(bookId)
                .count(count)
                .avg(avg)
                .build();
    }


}
