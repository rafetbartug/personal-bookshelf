package com.rbb.bookshelf.rating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRatingRequest {
    @NotNull
    private Long bookId;

    @NotNull
    @Min(0) @Max(5)
    private Integer score;

    private String comment;
}