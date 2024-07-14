package com.bloggy.blog;

import java.util.List;

public interface IBlogService {
    BlogResponse createBlog(BlogRequest newBlog, String email);

    BlogResponse updateBlog(BlogRequest updatedBlog, Long blogId, String email);

    void deleteBlog(Long blogId, String email);

    List<BlogResponse> getUserBlogs(String email);
}
