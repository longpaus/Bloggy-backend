package com.bloggy.blog;

import com.bloggy.blog.dto.BlogMetaData;
import com.bloggy.blog.dto.BlogRequest;
import com.bloggy.blog.dto.BlogResponse;
import com.bloggy.blogVersion.BlogVersionRequest;
import com.bloggy.blogVersion.BlogVersionResponse;
import com.bloggy.publishedBlog.PublishedBlogRequest;
import com.bloggy.publishedBlog.PublishedBlogResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController("/api/v1")
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/user/blogs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BlogMetaData>> getBlogs(
            Authentication auth,
            @RequestParam(required = false, defaultValue = "20") int offset,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(blogService.getUserBlogs(auth.getName(), offset, limit));
    }

    @PostMapping("/blogs")
    @ResponseStatus(HttpStatus.CREATED)
    @Description("create a new blog")
    public ResponseEntity<BlogResponse> createBlog(Authentication auth, @RequestBody BlogRequest blogRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.createBlog(blogRequest, auth.getName()));
    }

    @PutMapping("/blogs/{blogId}")
    @ResponseStatus(HttpStatus.OK)
    @Description("updates blog")
    public ResponseEntity<BlogResponse> updateBlog(Authentication auth, @PathVariable Long blogId, @RequestBody BlogRequest blogRequest) {
        return ResponseEntity.ok(blogService.updateBlog(blogRequest, blogId, auth.getName()));
    }

    @GetMapping("/blogs/{blogId}/versions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BlogVersionResponse>> getBlogVersions(
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "20") int offset,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(blogService.getBlogIdVersions(blogId, offset, limit));
    }

    @PostMapping("/blogs/{blogId}/publish")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PublishedBlogResponse> publishBlog(
            Authentication auth,
            @PathVariable Long blogId,
            @Valid @RequestBody PublishedBlogRequest blogRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.publishBlog(blogRequest, blogId, auth.getName()));
    }

    @PostMapping("/blogs/{blogId}/versions")
    @Description("save a blog content")
    public ResponseEntity<BlogVersionResponse> saveBlogVersion(
            Authentication auth,
            @PathVariable Long blogId,
            @Valid @RequestBody BlogVersionRequest blogVersionRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.saveBlog(blogVersionRequest, blogId, auth.getName()));
    }
}
