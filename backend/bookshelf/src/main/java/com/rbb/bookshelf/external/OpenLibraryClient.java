package com.rbb.bookshelf.external;

import com.rbb.bookshelf.external.dto.ExternalBookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenLibraryClient {

    private final RestClient restClient = RestClient.create();

    @Value("${external.openlibrary.base-url:https://openlibrary.org}")
    private String baseUrl;

    @SuppressWarnings("unchecked")
    public ExternalBookResponse fetchByIsbn(String isbn) {
        String url = baseUrl + "/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";

        Map<String, Object> root = restClient.get()
                .uri(url)
                .retrieve()
                .body(Map.class);

        String key = "ISBN:" + isbn;
        Object raw = root != null ? root.get(key) : null;
        if (!(raw instanceof Map)) {
            return ExternalBookResponse.builder()
                    .isbn(isbn)
                    .source("OPEN_LIBRARY")
                    .build();
        }

        Map<String, Object> data = (Map<String, Object>) raw;

        String title = (String) data.get("title");

        Integer firstPublishYear = null;
        Object fp = data.get("publish_date");
        // OpenLibrary bazen "1949" bazen "June 1949" gibi string donuyor; yil yakalamaya calis
        if (fp instanceof String s) {
            String digits = s.replaceAll("[^0-9]", "");
            if (digits.length() >= 4) firstPublishYear = Integer.parseInt(digits.substring(0, 4));
        }

        List<String> authors = Collections.emptyList();
        Object authorsObj = data.get("authors");
        if (authorsObj instanceof List<?> list) {
            authors = list.stream()
                    .filter(x -> x instanceof Map)
                    .map(x -> (Map<String, Object>) x)
                    .map(m -> (String) m.get("name"))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        String coverUrl = null;
        Object coverObj = data.get("cover");
        if (coverObj instanceof Map<?, ?> c) {
            Object large = c.get("large");
            Object medium = c.get("medium");
            Object small = c.get("small");
            coverUrl = (large instanceof String) ? (String) large
                    : (medium instanceof String) ? (String) medium
                    : (small instanceof String) ? (String) small
                    : null;
        }

        return ExternalBookResponse.builder()
                .isbn(isbn)
                .title(title)
                .authors(authors)
                .firstPublishYear(firstPublishYear)
                .coverUrl(coverUrl)
                .source("OPEN_LIBRARY")
                .build();
    }
}
