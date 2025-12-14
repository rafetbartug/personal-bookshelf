package com.rbb.bookshelf.rating;

import com.rbb.bookshelf.common.CurrentUserService;
import com.rbb.bookshelf.rating.dto.CreateRatingRequest;
import com.rbb.bookshelf.rating.dto.RatingResponse;
import com.rbb.bookshelf.rating.dto.RatingSummaryResponse;
import com.rbb.bookshelf.rating.dto.UpdateRatingRequest;
import com.rbb.bookshelf.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final CurrentUserService currentUserService;

    // Puan Ver
    @PostMapping
    public RatingResponse rate(@Valid @RequestBody CreateRatingRequest req) {
        User user = currentUserService.getCurrentUser();
        return ratingService.rate(user, req.getBookId(), req.getScore(), req.getComment());
    }

    // Bir kitabın yorumlarını getir
    @GetMapping("/book/{bookId}")
    public List<RatingResponse> list(@PathVariable Long bookId) {
        return ratingService.listForBook(bookId);
    }

    // Bir kitabın özetini (Ortalama puan vs.) getir
    @GetMapping("/book/{bookId}/summary")
    public RatingSummaryResponse summary(@PathVariable Long bookId) {
        return ratingService.summary(bookId);
    }

    // Benim bu kitaba verdiğim puanı getir (Frontend'de göstermek için)
    @GetMapping("/book/{bookId}/my")
    public RatingResponse myRating(@PathVariable Long bookId) {
        User user = currentUserService.getCurrentUser();
        return ratingService.myRating(user.getId(), bookId);
    }

    // Puanımı Güncelle
    @PutMapping("/book/{bookId}")
    public RatingResponse update(@PathVariable Long bookId, @Valid @RequestBody UpdateRatingRequest req) {
        User user = currentUserService.getCurrentUser();
        return ratingService.updateMyRating(user.getId(), bookId, req.getScore(), req.getComment());
    }

    // Puanımı Sil
    @DeleteMapping("/book/{bookId}")
    public void delete(@PathVariable Long bookId) {
        User user = currentUserService.getCurrentUser();
        ratingService.deleteMyRating(user.getId(), bookId);
    }

    // Profil sayfası için: Benim tüm yorumlarım
    @GetMapping("/my-ratings")
    public List<RatingResponse> getMyRatings() {
        User user = currentUserService.getCurrentUser();
        return ratingService.getMyAllRatings(user.getId());
    }
}