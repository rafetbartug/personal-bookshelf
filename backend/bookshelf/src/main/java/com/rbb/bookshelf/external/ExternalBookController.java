package com.rbb.bookshelf.external;

import com.rbb.bookshelf.external.dto.ExternalBookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external/books")
public class ExternalBookController {

    private final OpenLibraryClient openLibraryClient;

    // public olsun: arama ekrani gibi yerlerde token istemeden calisir
    @GetMapping("/isbn/{isbn}")
    public ExternalBookResponse byIsbn(@PathVariable String isbn) {
        return openLibraryClient.fetchByIsbn(isbn);
    }
}
