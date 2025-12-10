package com.rbb.bookshelf.rating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateRatingRequest {
    @Min(0) @Max(10)
    private Integer score;

    private String comment;
}
