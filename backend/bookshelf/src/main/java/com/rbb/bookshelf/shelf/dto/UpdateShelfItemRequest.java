package com.rbb.bookshelf.shelf.dto;

import com.rbb.bookshelf.shelf.ReadingStatus;
import lombok.Data;

@Data
public class UpdateShelfItemRequest {
    private ReadingStatus status;
    private Integer progressPercent;
}
