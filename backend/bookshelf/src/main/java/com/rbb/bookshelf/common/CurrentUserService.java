package com.rbb.bookshelf.common;

import com.rbb.bookshelf.user.User;
import com.rbb.bookshelf.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    // --- YENİ METOT (RatingController ve diğer yeni yerler için) ---
    // Parametre almaz, SecurityContext'ten kendisi bulur.
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return requireUser(authentication); // Alttaki metodu kullanarak kod tekrarını önlüyoruz
    }

    // --- ESKİ METOT (ShelfController ve eski yerler için) ---
    // Authentication nesnesini parametre olarak bekler.
    public User requireUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Kullanıcı girişi yapılmamış!");
        }

        String username;
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username))
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + username));
    }

    // --- ESKİ METOT (ShelfController için ID döndüren) ---
    public Long requireUserId(Authentication auth) {
        return requireUser(auth).getId();
    }
}