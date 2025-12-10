package com.rbb.bookshelf.shelf;

import com.rbb.bookshelf.common.CurrentUserService;
import com.rbb.bookshelf.shelf.dto.CreateShelfRequest;
import com.rbb.bookshelf.shelf.dto.ShelfResponse;
import com.rbb.bookshelf.shelf.dto.UpdateShelfRequest;
import com.rbb.bookshelf.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelves")
@RequiredArgsConstructor
public class ShelfController {

    private final ShelfService shelfService;
    private final CurrentUserService currentUserService;

    @PostMapping
    public ShelfResponse create(@Valid @RequestBody CreateShelfRequest req, Authentication auth) {
        User user = currentUserService.requireUser(auth);
        return shelfService.create(user, req.getName(), req.isPublic());
    }

    @GetMapping
    public List<ShelfResponse> listMine(Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        return shelfService.listMine(userId);
    }

    @GetMapping("/{id}")
    public ShelfResponse getMine(@PathVariable Long id, Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        return shelfService.getMine(userId, id);
    }

    @PutMapping("/{id}")
    public ShelfResponse updateMine(@PathVariable Long id, @Valid @RequestBody UpdateShelfRequest req, Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        return shelfService.updateMine(userId, id, req.getName(), req.isPublic());
    }

    @DeleteMapping("/{id}")
    public void deleteMine(@PathVariable Long id, Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        shelfService.deleteMine(userId, id);
    }
}
