package com.rbb.bookshelf.book;

import com.rbb.bookshelf.book.dto.BookRequest;
import com.rbb.bookshelf.book.dto.BookResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public BookResponse create(@Valid @RequestBody BookRequest req) {
        return bookService.create(req);
    }

    @GetMapping
    public List<BookResponse> list() {
        return bookService.list();
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return bookService.get(id);
    }
}
