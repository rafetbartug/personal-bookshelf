package com.rbb.bookshelf.shelf;

import com.rbb.bookshelf.book.Book;
import com.rbb.bookshelf.book.BookRepository;
import com.rbb.bookshelf.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelfItemService {

    private final ShelfService shelfService;
    private final ShelfItemRepository shelfItemRepository;
    private final BookRepository bookRepository;

    public ShelfItem addItem(Long userId, Long shelfId, Long bookId, ReadingStatus status, Integer progressPercent) {
        Shelf shelf = shelfService.getMine(userId, shelfId);

        if (shelfItemRepository.existsByShelfIdAndBookId(shelfId, bookId)) {
            // basit: ayni kitap ayni rafta varsa tekrar ekleme
            throw new IllegalArgumentException("Book already exists in this shelf");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        ShelfItem item = ShelfItem.builder()
                .shelf(shelf)
                .book(book)
                .status(status)
                .progressPercent(progressPercent)
                .build();

        return shelfItemRepository.save(item);
    }

    public List<ShelfItem> listItems(Long userId, Long shelfId) {
        shelfService.getMine(userId, shelfId); // owner check
        return shelfItemRepository.findAllByShelfId(shelfId);
    }

    public ShelfItem updateItem(Long userId, Long shelfId, Long itemId, ReadingStatus status, Integer progressPercent) {
        shelfService.getMine(userId, shelfId); // owner check

        ShelfItem item = shelfItemRepository.findByIdAndShelfId(itemId, shelfId)
                .orElseThrow(() -> new NotFoundException("Shelf item not found"));

        if (status != null) item.setStatus(status);
        if (progressPercent != null) item.setProgressPercent(progressPercent);

        return shelfItemRepository.save(item);
    }

    public void removeItem(Long userId, Long shelfId, Long itemId) {
        shelfService.getMine(userId, shelfId); // owner check
        ShelfItem item = shelfItemRepository.findByIdAndShelfId(itemId, shelfId)
                .orElseThrow(() -> new NotFoundException("Shelf item not found"));
        shelfItemRepository.delete(item);
    }
}
