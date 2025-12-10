package com.rbb.bookshelf.rating.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingSummaryResponse {
    private Long bookId;
    private long count;
    private Double avg;
}
