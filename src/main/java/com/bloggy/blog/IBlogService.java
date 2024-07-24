package com.bloggy.blog;

import com.bloggy.blogVersion.BlogVersionRequest;
import com.bloggy.blogVersion.BlogVersionResponse;
import com.bloggy.publishedBlog.PublishedBlogRequest;
import com.bloggy.publishedBlog.PublishedBlogResponse;

import java.util.List;

public interface IBlogService {
    BlogResponse createBlog(BlogRequest newBlog, String email);

    BlogResponse updateBlog(BlogRequest updatedBlog, Long blogId, String email);

    void deleteBlog(Long blogId, String email);


    List<BlogVersionResponse> getUserBlogs(String email, int offset, int limit);

    BlogVersionResponse saveBlog(BlogVersionRequest request, Long blogId, String email);

    /**
     * Use to publish a blog, if the published content is up-to-date then a new version will not be created. However, if the published blog is different to the latest version stored then a new version will be created
     *
     * @param blogId  - The id of the blog we are publishing
     * @param request - The request body
     * @param email   - The email of the user, we get this from the username of the Authentication object
     * @return a PublishedBlogResponse object
     */
    PublishedBlogResponse publishBlog(Long blogId, PublishedBlogRequest request, String email);

    List<BlogVersionResponse> getBlogVersions(Long blogId, int offset, int limit, String email);
}
