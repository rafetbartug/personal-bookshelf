package com.rbb.bookshelf.shelf.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateShelfRequest {
    @NotBlank
    private String name;
    private boolean isPublic;
}
