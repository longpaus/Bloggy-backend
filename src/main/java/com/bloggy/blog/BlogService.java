package com.bloggy.blog;

import com.bloggy.blog.dto.BlogMetaData;
import com.bloggy.blog.dto.BlogRequest;
import com.bloggy.blog.dto.BlogResponse;
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
        Blog blog = blogRepository.save(blogMapper.BlogReqestToBlog(newBlog, user));

        blogVersionRepository.save(blogVersionMapper.blogRequestToBlogVersion(newBlog, blog, LocalDateTime.now()));
        return blogMapper.blogToBlogResponse(blog, newBlog.getContent(), user);
    }

    @Override
    public BlogResponse updateBlog(BlogRequest blogRequest, Long blogId, String email) {
        User user = getUserByEmail(email);
        Blog blog = getBlogById(blogId);
        if (!user.getId().equals(blog.getUser().getId())) {
            throw new UnauthorizedException("user not authorized to update this blog");
        }
        BlogVersion latestVersion = getLatestBlogVersion(blogId);
        if (!latestVersion.getContent().equals(blogRequest.getContent())) {
            blogVersionRepository.save(blogVersionMapper.blogRequestToBlogVersion(blogRequest, blog, LocalDateTime.now()));
        }
        return blogMapper.blogToBlogResponse(blog, blogRequest.getContent(), user);
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
    public List<BlogMetaData> getUserBlogs(String email, int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new RuntimeException("offset or limit is negative");
        }
        User user = getUserByEmail(email);

        Pageable pageable = PageRequest.of(offset / limit, limit);
        return blogRepository
                .findAllByUserId(user.getId(), pageable)
                .stream()
                .map(blogMapper::blogToBlogMetaData)
                .toList();
    }

    @Override
    public List<BlogVersionResponse> getBlogIdVersions(Long blogId, int offset, int limit) {
        // check if blogId is valid
        getBlogById(blogId);
        Pageable pageable = PageRequest.of(offset / limit, limit);

        return blogVersionRepository
                .findByBlogId(blogId, pageable)
                .stream()
                .map(blogVersionMapper::blogVersionToResponse)
                .toList();
    }

    @Override
    public PublishedBlogResponse publishBlog(PublishedBlogRequest publishedBlogRequest, Long blogId, String email) {
        getUserByEmail(email);
        Blog blog = getBlogById(blogId);
        BlogVersion version = blogVersionRepository.findByVersionId(publishedBlogRequest.getVersionId());

        PublishedBlog publishedBlog = publishedBlogRepository.save(publishedBlogMapper.fromRequest(publishedBlogRequest, blog, version, LocalDateTime.now()));
        return publishedBlogMapper.toResponse(publishedBlog);
    }

    @Override
    public BlogVersionResponse saveBlog(BlogVersionRequest request, Long blogId, String email) {

        User user = getUserByEmail(email);
        Blog blog = getBlogById(blogId);

        if (!user.getId().equals(blog.getUser().getId())) {
            throw new UnauthorizedException("User not authorized to update this blog");
        }

        BlogVersion newVersion = blogVersionMapper.fromRequest(request, blog, LocalDateTime.now());
        return blogVersionMapper.toResponse(newVersion);
    }


    private BlogVersion getLatestBlogVersion(Long blogId) {
        return blogVersionRepository.findFirstByBlogOrderByTimeDesc(blogId);
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
