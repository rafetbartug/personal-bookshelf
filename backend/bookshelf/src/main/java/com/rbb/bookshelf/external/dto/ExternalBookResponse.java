package com.rbb.bookshelf.external.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExternalBookResponse {
    private String isbn;
    private String title;
    private List<String> authors;
    private Integer firstPublishYear;
    private String coverUrl;
    private String source; // "OPEN_LIBRARY"
}
