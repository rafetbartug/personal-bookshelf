package com.rbb.bookshelf.rating.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class RatingResponse {
    private Long id;
    private Integer score;
    private String comment;
    private LocalDateTime ratedAt;

    private Long bookId;
    private String bookTitle;
    private String bookCoverUrl;

    private Long userId;
    private String username;
}