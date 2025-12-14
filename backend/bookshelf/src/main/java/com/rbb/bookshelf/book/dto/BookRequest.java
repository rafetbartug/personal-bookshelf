package com.rbb.bookshelf.book.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank
    private String title;

    private String isbn;
    private Integer publishedYear;
    private String coverUrl;
    private String description;
    private Long authorId;
    private String authorName;
}