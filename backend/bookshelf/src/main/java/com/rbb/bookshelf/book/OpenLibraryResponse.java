package com.rbb.bookshelf.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// OpenLibrary'den gelen ana cevap kutusu
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryResponse {
    public List<Doc> docs;

    // Her bir kitap sonucu
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Doc {
        public String title;
        public List<String> author_name;
        public List<String> isbn;
        public Integer first_publish_year;
        public Integer cover_i; // Kapak resmi ID'si
    }
}