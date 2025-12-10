package com.rbb.bookshelf.rating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateRatingRequest {
    @Min(0) @Max(10)
    private Integer score;

    private String comment;
}
