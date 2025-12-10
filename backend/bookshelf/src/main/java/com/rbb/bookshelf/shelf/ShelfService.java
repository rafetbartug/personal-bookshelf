package com.rbb.bookshelf.shelf;

import com.rbb.bookshelf.common.NotFoundException;
import com.rbb.bookshelf.shelf.dto.ShelfResponse;
import org.springframework.transaction.annotation.Transactional;
import com.rbb.bookshelf.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final ShelfItemRepository shelfItemRepository;
    public ShelfResponse create(User user, String name, boolean isPublic) {
        Shelf shelf = Shelf.builder()
                .user(user)
                .name(name)
                .isPublic(isPublic)
                .build();
        return toResponse(shelfRepository.save(shelf));
    }

    public List<ShelfResponse> listMine(Long userId) {
        return shelfRepository.findAllByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ShelfResponse getMine(Long userId, Long shelfId) {
        return toResponse(getMineEntity(userId, shelfId));
    }

    public ShelfResponse updateMine(Long userId, Long shelfId, String name, boolean isPublic) {
        Shelf shelf = getMineEntity(userId, shelfId);
        shelf.setName(name);
        shelf.setPublic(isPublic);
        return toResponse(shelfRepository.save(shelf));
    }

    @Transactional
    public void deleteMine(Long userId, Long shelfId) {
        Shelf shelf = getMineEntity(userId, shelfId);
        shelfItemRepository.deleteAllByShelfId(shelfId);
        shelfRepository.delete(shelf);
    }

    public Shelf getMineEntity(Long userId, Long shelfId) {
        return shelfRepository.findByIdAndUserId(shelfId, userId)
                .orElseThrow(() -> new NotFoundException("Shelf not found"));
    }

    private ShelfResponse toResponse(Shelf s) {
        return ShelfResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .isPublic(s.isPublic())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
