package com.rbb.bookshelf.shelf;

import com.rbb.bookshelf.common.NotFoundException;
import com.rbb.bookshelf.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfRepository shelfRepository;

    public Shelf create(User user, String name, boolean isPublic) {
        Shelf shelf = Shelf.builder()
                .user(user)
                .name(name)
                .isPublic(isPublic)
                .build();
        return shelfRepository.save(shelf);
    }

    public List<Shelf> listMine(Long userId) {
        return shelfRepository.findAllByUserId(userId);
    }

    public Shelf getMine(Long userId, Long shelfId) {
        return shelfRepository.findByIdAndUserId(shelfId, userId)
                .orElseThrow(() -> new NotFoundException("Shelf not found"));
    }

    public Shelf updateMine(Long userId, Long shelfId, String name, boolean isPublic) {
        Shelf shelf = getMine(userId, shelfId);
        shelf.setName(name);
        shelf.setPublic(isPublic);
        return shelfRepository.save(shelf);
    }

    public void deleteMine(Long userId, Long shelfId) {
        Shelf shelf = getMine(userId, shelfId);
        shelfRepository.delete(shelf);
    }
}
