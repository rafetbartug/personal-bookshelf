package com.rbb.bookshelf.shelf.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShelfResponse {
    private Long id;
    private String name;
    private boolean isPublic;
}
