package com.rbb.bookshelf.book;

import com.rbb.bookshelf.author.AuthorRepository;
import com.rbb.bookshelf.book.dto.BookRequest;
import com.rbb.bookshelf.book.dto.BookResponse;
import com.rbb.bookshelf.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private BookResponse toResponse(Book b) {
        return BookResponse.builder()
                .id(b.getId())
                .title(b.getTitle())
                .isbn(b.getIsbn())
                .publishedYear(b.getPublishedYear())
                .coverUrl(b.getCoverUrl())
                .description(b.getDescription())
                .authorId(b.getAuthor().getId())
                .authorName(b.getAuthor().getName())
                .build();
    }

    public BookResponse create(BookRequest req) {
        Author author = authorRepository.findById(req.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author not found"));

        Book b = Book.builder()
                .title(req.getTitle())
                .isbn(req.getIsbn())
                .publishedYear(req.getPublishedYear())
                .coverUrl(req.getCoverUrl())
                .description(req.getDescription())
                .author(author)
                .build();

        return toResponse(bookRepository.save(b));
    }

    public List<BookResponse> list() {
        return bookRepository.findAll().stream().map(this::toResponse).toList();
    }

    public BookResponse get(Long id) {
        Book b = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        return toResponse(b);
    }
    public BookResponse update(Long id, BookRequest req) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        // Yazar değişmiş olabilir, kontrol et
        if (!book.getAuthor().getId().equals(req.getAuthorId())) {
            Author newAuthor = authorRepository.findById(req.getAuthorId())
                    .orElseThrow(() -> new NotFoundException("Author not found"));
            book.setAuthor(newAuthor);
        }

        book.setTitle(req.getTitle());
        book.setIsbn(req.getIsbn());
        book.setPublishedYear(req.getPublishedYear());
        book.setCoverUrl(req.getCoverUrl());
        book.setDescription(req.getDescription());

        return toResponse(bookRepository.save(book));
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }
        bookRepository.deleteById(id);
    }
}
