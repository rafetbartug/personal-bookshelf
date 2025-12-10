package com.rbb.bookshelf.shelf.dto;

import com.rbb.bookshelf.shelf.ReadingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShelfItemResponse {
    private Long id;
    private LocalDateTime addedAt;
    private ReadingStatus status;
    private Integer progressPercent;

    private Long bookId;
    private String bookTitle;
    private Long authorId;
    private String authorName;
}
