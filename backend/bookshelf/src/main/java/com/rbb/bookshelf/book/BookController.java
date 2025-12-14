package com.rbb.bookshelf.book;

import com.rbb.bookshelf.author.AuthorRepository;
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
    private final OpenLibraryService openLibraryService;

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Book createBook(@Valid @RequestBody BookRequest request) {
        Author author = null;

          if (request.getAuthorId() != null) {
            author = authorRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Yazar bulunamadı!"));
        }
        else if (request.getAuthorName() != null && !request.getAuthorName().isEmpty()) {

            author = authorRepository.findByName(request.getAuthorName())
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(request.getAuthorName());
                        newAuthor.setBio("Otomatik oluşturuldu (OpenLibrary)");
                        return authorRepository.save(newAuthor);
                    });
        }
        else {
            throw new RuntimeException("Yazar bilgisi zorunludur!");
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setCoverUrl(request.getCoverUrl());
        book.setDescription(request.getDescription());
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    @GetMapping
    public List<BookResponse> list() {
        return bookService.list();
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return bookService.get(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest req) {
        return bookService.update(id, req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping("/search-external")
    public List<BookRequest> searchFromOpenLibrary(@RequestParam String query) {
        return openLibraryService.searchBooks(query);
    }
}