package com.rbb.bookshelf.book;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import com.rbb.bookshelf.book.dto.BookRequest;
@Service
public class OpenLibraryService {

    private final String API_URL = "https://openlibrary.org/search.json?q=";

    public List<BookRequest> searchBooks(String query) {
        RestTemplate restTemplate = new RestTemplate();
        // Boşlukları URL formatına çevir (Harry Potter -> Harry+Potter)
        String url = API_URL + query.replace(" ", "+");

        OpenLibraryResponse response = restTemplate.getForObject(url, OpenLibraryResponse.class);

        List<BookRequest> results = new ArrayList<>();

        if (response != null && response.docs != null) {
            // İlk 10 sonucu alalım (çok fazla veri gelmesin)
            response.docs.stream().limit(10).forEach(doc -> {
                BookRequest book = new BookRequest();
                book.setTitle(doc.title);

                // Yazar varsa hem Açıklamaya hem de isme ekle
                if (doc.author_name != null && !doc.author_name.isEmpty()) {
                    String author = doc.author_name.get(0);
                    book.setAuthorName(author); // <-- YENİ: İsmi buraya kaydediyoruz
                    book.setDescription("Yazar: " + author);
                }

                if (doc.isbn != null && !doc.isbn.isEmpty()) {
                    book.setIsbn(doc.isbn.get(0));
                }
                book.setPublishedYear(doc.first_publish_year);

                if (doc.cover_i != null) {
                    book.setCoverUrl("https://covers.openlibrary.org/b/id/" + doc.cover_i + "-L.jpg");
                }

                results.add(book);
            });
        }
        return results;
    }
}