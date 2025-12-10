package com.rbb.bookshelf.shelf.dto;

import com.rbb.bookshelf.shelf.ReadingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddShelfItemRequest {
    @NotNull
    private Long bookId;

    private ReadingStatus status;
    private Integer progressPercent;
}
