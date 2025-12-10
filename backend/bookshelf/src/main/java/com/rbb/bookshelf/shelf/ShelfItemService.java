package com.rbb.bookshelf.shelf;

import com.rbb.bookshelf.book.Book;
import com.rbb.bookshelf.book.BookRepository;
import com.rbb.bookshelf.common.NotFoundException;
import com.rbb.bookshelf.shelf.dto.ShelfItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelfItemService {

    private final ShelfService shelfService;
    private final ShelfItemRepository shelfItemRepository;
    private final BookRepository bookRepository;

    private ShelfItemResponse toResponse(ShelfItem item) {
        return ShelfItemResponse.builder()
                .id(item.getId())
                .addedAt(item.getAddedAt())
                .status(item.getStatus())
                .progressPercent(item.getProgressPercent())
                .bookId(item.getBook().getId())
                .bookTitle(item.getBook().getTitle())
                .authorId(item.getBook().getAuthor().getId())
                .authorName(item.getBook().getAuthor().getName())
                .build();
    }

    public ShelfItemResponse addItem(Long userId, Long shelfId, Long bookId, ReadingStatus status, Integer progressPercent) {
        Shelf shelf = shelfService.getMineEntity(userId, shelfId); // asagida acikliyorum

        if (shelfItemRepository.existsByShelfIdAndBookId(shelfId, bookId)) {
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

        return toResponse(shelfItemRepository.save(item));
    }

    public List<ShelfItemResponse> listItems(Long userId, Long shelfId) {
        shelfService.getMineEntity(userId, shelfId); // owner check
        return shelfItemRepository.findAllByShelfId(shelfId).stream().map(this::toResponse).toList();
    }

    public ShelfItemResponse updateItem(Long userId, Long shelfId, Long itemId, ReadingStatus status, Integer progressPercent) {
        shelfService.getMineEntity(userId, shelfId); // owner check

        ShelfItem item = shelfItemRepository.findByIdAndShelfId(itemId, shelfId)
                .orElseThrow(() -> new NotFoundException("Shelf item not found"));

        if (status != null) item.setStatus(status);
        if (progressPercent != null) item.setProgressPercent(progressPercent);

        return toResponse(shelfItemRepository.save(item));
    }

    public void removeItem(Long userId, Long shelfId, Long itemId) {
        shelfService.getMineEntity(userId, shelfId); // owner check
        ShelfItem item = shelfItemRepository.findByIdAndShelfId(itemId, shelfId)
                .orElseThrow(() -> new NotFoundException("Shelf item not found"));
        shelfItemRepository.delete(item);
    }
}
