package com.rbb.bookshelf.shelf.dto;

import com.rbb.bookshelf.shelf.ReadingStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShelfItemResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private ReadingStatus status;
    private Integer progressPercent;
}
