package com.rbb.bookshelf.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // Thymeleaf'e dinamik veri gönderelim (Server-side rendering kanıtı)
        model.addAttribute("appName", "Personal Bookshelf");
        model.addAttribute("serverTime", LocalDateTime.now());
        return "home"; // home.html dosyasını arayacak
    }
}