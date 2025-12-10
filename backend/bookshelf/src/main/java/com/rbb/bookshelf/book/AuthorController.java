package com.rbb.bookshelf.book;

import com.rbb.bookshelf.book.dto.AuthorRequest;
import com.rbb.bookshelf.book.dto.AuthorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public AuthorResponse create(@Valid @RequestBody AuthorRequest req) {
        return authorService.create(req);
    }

    @GetMapping
    public List<AuthorResponse> list() {
        return authorService.list();
    }

    @GetMapping("/{id}")
    public AuthorResponse get(@PathVariable Long id) {
        return authorService.get(id);
    }
}
