package com.bloggy.blog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController {
    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }
}
