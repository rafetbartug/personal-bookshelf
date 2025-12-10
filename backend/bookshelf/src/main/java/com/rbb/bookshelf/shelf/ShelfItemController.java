package com.rbb.bookshelf.shelf;

import com.rbb.bookshelf.common.CurrentUserService;
import com.rbb.bookshelf.shelf.dto.AddShelfItemRequest;
import com.rbb.bookshelf.shelf.dto.ShelfItemResponse;
import com.rbb.bookshelf.shelf.dto.UpdateShelfItemRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelves/{shelfId}/items")
@RequiredArgsConstructor
public class ShelfItemController {

    private final ShelfItemService shelfItemService;
    private final CurrentUserService currentUserService;

    @PostMapping
    public ShelfItemResponse add(@PathVariable Long shelfId, @Valid @RequestBody AddShelfItemRequest req, Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        return shelfItemService.addItem(userId, shelfId, req.getBookId(), req.getStatus(), req.getProgressPercent());
    }

    @GetMapping
    public List<ShelfItemResponse> list(@PathVariable Long shelfId, Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        return shelfItemService.listItems(userId, shelfId);
    }

    @PatchMapping("/{itemId}")
    public ShelfItemResponse update(
            @PathVariable Long shelfId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateShelfItemRequest req,
            Authentication auth
    ) {
        Long userId = currentUserService.requireUserId(auth);
        return shelfItemService.updateItem(userId, shelfId, itemId, req.getStatus(), req.getProgressPercent());
    }

    @DeleteMapping("/{itemId}")
    public void remove(@PathVariable Long shelfId, @PathVariable Long itemId, Authentication auth) {
        Long userId = currentUserService.requireUserId(auth);
        shelfItemService.removeItem(userId, shelfId, itemId);
    }
}
