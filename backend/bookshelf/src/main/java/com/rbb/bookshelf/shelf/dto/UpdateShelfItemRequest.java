package com.rbb.bookshelf.shelf.dto;

import com.rbb.bookshelf.shelf.ReadingStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateShelfItemRequest {

    private ReadingStatus status;

    @Min(0)
    @Max(100)
    private Integer progressPercent;
}
