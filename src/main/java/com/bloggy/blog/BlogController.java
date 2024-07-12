package com.bloggy.blog;

import com.bloggy.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/blogs")
@RestController
@RequiredArgsConstructor
public class BlogController {

    private final IBlogService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public ResponseEntity<List<BlogResponse>> getAllBlogs(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllBlogs(principal.getName()));
    }

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(Principal principal, @Valid @RequestBody BlogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBlog(request, principal.getName()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<BlogResponse> updateBlog(Principal principal, @Valid @RequestBody BlogRequest request, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateBlog(request,id,principal.getName()));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(Principal principal, @PathVariable Long id) {
        service.deleteBlog(id,principal.getName());
        return ResponseEntity.accepted().build();
    }

}
