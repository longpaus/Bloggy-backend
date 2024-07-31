package com.bloggy.blog;

import com.bloggy.blog.dto.BlogMetaData;
import com.bloggy.blog.dto.BlogRequest;
import com.bloggy.blog.dto.BlogResponse;
import com.bloggy.blogVersion.BlogVersionRequest;
import com.bloggy.blogVersion.BlogVersionResponse;
import com.bloggy.exception.IdNotFoundException;
import com.bloggy.exception.UnauthorizedException;
import com.bloggy.publishedBlog.PublishedBlogRequest;
import com.bloggy.publishedBlog.PublishedBlogResponse;

import java.util.List;

public interface IBlogService {

    /**
     * Create a new blog entry. Should create a new BlogVersion and save it.
     *
     * @param blogRequest - the blog request body
     * @param email       - user email
     * @return a blog response object
     * @throws IdNotFoundException if the user email is not valid
     */
    BlogResponse createBlog(BlogRequest blogRequest, String email);

    /**
     * Updates a blog. If the content is updated then a new blog version will be created and saved.
     *
     * @param blogRequest - contains the attributes of a blog to be updated
     * @param blogId      - id of the blog to be updated
     * @param email       - user email
     * @return a blog response object
     * @throws IdNotFoundException   if the user email or blogId is not valid
     * @throws UnauthorizedException if the user is not authorized to update this blog
     */
    BlogResponse updateBlog(BlogRequest blogRequest, Long blogId, String email);

    void deleteBlog(Long blogId, String email);

    /**
     * Get the metadata of user blogs with pagination
     *
     * @param email  - the user email
     * @param offset - the page number
     * @param limit  - the size of a page
     * @return a list of BlogMetaData
     */
    List<BlogMetaData> getUserBlogs(String email, int offset, int limit);

    /**
     * Given a blog id, get the blog versions of that blog id with pagination
     *
     * @param blogId - id of the blog that we want the versions of
     * @param offset - the page number
     * @param limit  - size of each page
     * @param email  - the email of the user requesting the request
     * @return a BlogVersionResponse object
     */
    List<BlogVersionResponse> getBlogIdVersions(Long blogId, int offset, int limit, String email);

    /**
     * Given a VersionId, get the version for that versionId
     *
     * @param versionId
     * @param email-    the email of the user requesting the request
     * @return
     */
    BlogVersionResponse getBlogVersion(Long versionId, String email);

    PublishedBlogResponse publishBlog(PublishedBlogRequest publishedBlogRequest, Long blogId, String email);

    BlogVersionResponse saveBlog(BlogVersionRequest request, Long blogId, String email);

}
