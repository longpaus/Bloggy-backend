package com.bloggy.blog;

import com.bloggy.blogVersion.*;
import com.bloggy.exception.IdNotFoundException;
import com.bloggy.exception.UnauthorizedException;
import com.bloggy.publishedBlog.*;
import com.bloggy.user.IUserRepository;
import com.bloggy.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BlogService implements IBlogService {

    private final IBlogRepository blogRepository;
    private final IBlogMapper blogMapper;

    private final IUserRepository userRepository;

    private final IBlogVersionRepository blogVersionRepository;
    private final IBlogVersionMapper blogVersionMapper;

    private final IPublishedBlogRepository publishedBlogRepository;
    private final IPublishedBlogMapper publishedBlogMapper;

    @Override
    public BlogResponse createBlog(BlogRequest newBlog, String email) {
        User user = getUserByEmail(email);
        Blog blog = blogMapper.BlogReqestToBlog(newBlog, user);
        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.BlogToBlogResponse(savedBlog);
    }

    @Override
    public BlogResponse updateBlog(BlogRequest updatedBlog, Long blogId, String email) {
        User user = getUserByEmail(email);
        Blog blog = getBlogById(blogId);
        if (!user.getId().equals(blog.getUser().getId())) {
            throw new UnauthorizedException("user not authorized to update this blog");
        }
        Blog newBlog = blogMapper.BlogReqestToBlog(updatedBlog, user);
        return blogMapper.BlogToBlogResponse(blogRepository.save(newBlog));
    }

    @Override
    public void deleteBlog(Long blogId, String email) {
        User user = getUserByEmail(email);
        Blog blog = getBlogById(blogId);
        if (!user.getId().equals(blog.getUser().getId())) {
            throw new UnauthorizedException("user not authorized to delete blog");
        }
        blogRepository.delete(blog);
    }

    @Override
    public List<BlogVersionResponse> getUserBlogs(String email, int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new RuntimeException("offset or limit is negative");
        }
        User user = getUserByEmail(email);

        Pageable pageable = PageRequest.of(offset / limit, limit);
        return blogVersionRepository
                .findLatestBlogVersionsByUserId(user.getId(), pageable)
                .stream()
                .map(blogVersionMapper::toResponse)
                .toList();
    }

    @Override
    public BlogVersionResponse saveBlog(BlogVersionRequest request, Long blogId, String email) {

        User user = getUserByEmail(email);
        Blog blog = getBlogById(blogId);

        if (!user.getId().equals(blog.getUser().getId())) {
            throw new UnauthorizedException("User not authorized to update this blog");
        }

        BlogVersion newVersion = createNewVersion(blog, request);
        return blogVersionMapper.toResponse(newVersion);
    }

    @Override
    public PublishedBlogResponse publishBlog(Long blogId, PublishedBlogRequest request, String email) {
        User user = getUserByEmail(email);
        Blog blog = getBlogById(blogId);
        if (!user.getId().equals(blog.getUser().getId())) {
            throw new UnauthorizedException("User not authorized to publish this blog");
        }
        BlogVersion latestVersion = getLatestBlogVersion(blogId);
        if (!latestVersion.getContent().equals(request.getContent())) {
            latestVersion = createNewVersion(blog, request);
        }
        return publishedBlogMapper.toResponse(createNewPublishedBlog(blog, request, latestVersion));
    }


    @Override
    public List<BlogVersionResponse> getBlogVersions(Long blogId, int offset, int limit, String email) {
        getUserByEmail(email);
        getBlogById(blogId);

        Pageable pageable = PageRequest.of(offset / limit, limit);
        List<BlogVersion> versions = blogVersionRepository.findByBlogId(blogId, pageable);
        return versions
                .stream()
                .map(blogVersionMapper::toResponse)
                .toList();
    }

    private BlogVersion getLatestBlogVersion(Long blogId) {
        return blogVersionRepository.findFirstByBlogOrderByTimeDesc(blogId);
    }

    private PublishedBlog createNewPublishedBlog(Blog blog, PublishedBlogRequest request, BlogVersion version) {
        PublishedBlog publishedBlog = publishedBlogMapper.fromRequest(request, blog, version, LocalDateTime.now());
        return publishedBlogRepository.save(publishedBlog);
    }

    private BlogVersion createNewVersion(Blog blog, BlogVersionRequest request) {
        BlogVersion version = blogVersionMapper.fromRequest(request, blog, LocalDateTime.now());
        return blogVersionRepository.save(version);
    }

    private BlogVersion createNewVersion(Blog blog, PublishedBlogRequest request) {
        BlogVersion version = BlogVersion.builder()
                .blog(blog)
                .time(LocalDateTime.now())
                .content(request.getContent())
                .build();
        return blogVersionRepository.save(version);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IdNotFoundException("user email not found"));
    }

    private Blog getBlogById(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new IdNotFoundException("blog not found"));
    }
}
