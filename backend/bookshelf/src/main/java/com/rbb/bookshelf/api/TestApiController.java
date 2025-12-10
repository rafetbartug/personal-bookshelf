package com.rbb.bookshelf.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApiController {

    @GetMapping("/api/ping")
    public String ping() {
        return "pong";
    }
}
