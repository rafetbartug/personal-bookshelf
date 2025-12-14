package com.rbb.bookshelf.book;

import com.rbb.bookshelf.book.dto.AuthorRequest;
import com.rbb.bookshelf.book.dto.AuthorResponse;
import com.rbb.bookshelf.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.rbb.bookshelf.author.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private AuthorResponse toResponse(Author a) {
        return AuthorResponse.builder()
                .id(a.getId())
                .name(a.getName())
                .bio(a.getBio())
                .build();
    }

    public AuthorResponse create(AuthorRequest req) {
        Author a = Author.builder()
                .name(req.getName())
                .bio(req.getBio())
                .build();
        return toResponse(authorRepository.save(a));
    }

    public List<AuthorResponse> list() {
        return authorRepository.findAll().stream().map(this::toResponse).toList();
    }

    public AuthorResponse get(Long id) {
        Author a = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found"));
        return toResponse(a);
    }
    public AuthorResponse update(Long id, AuthorRequest req) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found"));

        author.setName(req.getName());
        author.setBio(req.getBio());

        return toResponse(authorRepository.save(author));
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new NotFoundException("Author not found");
        }
        // Not: Eğer yazarın kitapları varsa veritabanı (FK hatası) silmene izin vermeyebilir.
        // İstersen önce kitaplarını silebilirsin ama şimdilik basit tutalım.
        authorRepository.deleteById(id);
    }
}
