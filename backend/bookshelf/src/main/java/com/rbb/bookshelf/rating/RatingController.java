package com.rbb.bookshelf.rating;

import com.rbb.bookshelf.common.CurrentUserService;
import com.rbb.bookshelf.rating.dto.CreateRatingRequest;
import com.rbb.bookshelf.rating.dto.RatingResponse;
import com.rbb.bookshelf.rating.dto.RatingSummaryResponse;
import com.rbb.bookshelf.rating.dto.UpdateRatingRequest;
import com.rbb.bookshelf.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books/{bookId}/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final CurrentUserService currentUserService;

    @PostMapping
    public RatingResponse create(@PathVariable Long bookId, @Valid @RequestBody CreateRatingRequest req, Authentication auth) {
        User user = currentUserService.requireUser(auth);
        return ratingService.rate(user, bookId, req.getScore(), req.getComment());
    }

    @GetMapping
    public List<RatingResponse> list(@PathVariable Long bookId) {
        return ratingService.listForBook(bookId);
    }

    @GetMapping("/me")
    public RatingResponse me(@PathVariable Long bookId, Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        return ratingService.myRating(userId, bookId);
    }

    @PatchMapping("/me")
    public RatingResponse updateMe(@PathVariable Long bookId, @Valid @RequestBody UpdateRatingRequest req, Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        return ratingService.updateMyRating(userId, bookId, req.getScore(), req.getComment());
    }

    @DeleteMapping("/me")
    public void deleteMe(@PathVariable Long bookId, Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        ratingService.deleteMyRating(userId, bookId);
    }

    @GetMapping("/summary")
    public RatingSummaryResponse summary(@PathVariable Long bookId) {
        return ratingService.summary(bookId);
    }
}
