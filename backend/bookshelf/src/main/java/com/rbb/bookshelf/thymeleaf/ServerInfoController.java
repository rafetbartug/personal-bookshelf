package com.rbb.bookshelf.thymeleaf;

import com.rbb.bookshelf.book.BookRepository;
import com.rbb.bookshelf.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller // DİKKAT: RestController değil!
@RequiredArgsConstructor
public class ServerInfoController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @GetMapping("/server-info")
    public String showServerInfo(Model model) {
        // Thymeleaf sayfasına veri gönderiyoruz
        model.addAttribute("appName", "Personal Bookshelf System");
        model.addAttribute("serverTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("bookCount", bookRepository.count());
        model.addAttribute("status", "AKTİF 🟢");

        // "server-info" isminde bir HTML dosyası arayacak
        return "server-info";
    }
}