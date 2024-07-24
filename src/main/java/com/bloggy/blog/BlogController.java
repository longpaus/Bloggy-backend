package com.bloggy.blog;

import com.bloggy.blogVersion.BlogVersionRequest;
import com.bloggy.blogVersion.BlogVersionResponse;
import com.bloggy.publishedBlog.PublishedBlogRequest;
import com.bloggy.publishedBlog.PublishedBlogResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/blogs")
@RestController
@RequiredArgsConstructor
public class BlogController {

    private final IBlogService blogService;

    @PostMapping("/")
    @Description("create a new blog, this must be called before saving or publishing a blog")
    public ResponseEntity<BlogResponse> createBlog(Principal principal, @Valid @RequestBody BlogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.createBlog(request, principal.getName()));
    }

    @GetMapping
    @Description("Get blogs with pagination")
    public ResponseEntity<List<BlogVersionResponse>> getUserBlogs(Principal principal,
                                                                  @RequestParam(defaultValue = "0") int offset,
                                                                  @RequestParam(defaultValue = "20") int limit) {
        List<BlogVersionResponse> blogs = blogService.getUserBlogs(principal.getName(), offset, limit);
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/save/{blogId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BlogVersionResponse> saveBlog(Principal principal, @PathVariable Long blogId, @RequestBody BlogVersionRequest request) {
        return new ResponseEntity<>(blogService.saveBlog(request, blogId, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/save/versions/{blogId}")
    @Description("Get blog versions with pagination")
    public ResponseEntity<List<BlogVersionResponse>> getBlogVersions(
            Principal principal,
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<BlogVersionResponse> versions = blogService.getBlogVersions(blogId, offset, limit, principal.getName());
        return ResponseEntity.ok(versions);
    }

    @PostMapping("/publish/{blogId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Description("publish a blog")
    public ResponseEntity<PublishedBlogResponse> publishedBlog(
            Principal principal,
            @PathVariable Long blogId,
            @Valid @RequestBody PublishedBlogRequest request
    ) {
        return new ResponseEntity<>(blogService.publishBlog(blogId, request, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/publish/{blogId}")
    @ResponseStatus(HttpStatus.OK)
    @Description("given a blog id, get all the published versions of that blog")
    public ResponseEntity<List<BlogVersionResponse>> getBlogPublishedVersions(
            Principal principal,
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(blogService.getBlogVersions(blogId, offset, limit, principal.getName()));
    }

}
