package com.rbb.bookshelf.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank
    private String title;

    private String isbn;
    private Integer publishedYear;
    private String coverUrl;
    private String description;

    @NotNull
    private Long authorId;
}
