package com.bloggy.blogVersion;

import com.bloggy.blog.Blog;
import com.bloggy.blog.IBlogRepository;
import com.bloggy.exception.IdNotFoundException;
import com.bloggy.exception.UnauthorizedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class BlogVersionService implements IBlogVersionService {
    private final IBlogVersionRepository blogVersionRepository;
    private final IBlogRepository blogRepository;
    private final IBlogVersionMapper blogVersionMapper;

    @Override
    public BlogVersionResponse addVersion(BlogVersionRequest versionRequest, Long blogId, String userEmail) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IdNotFoundException("Blog Id not found"));
        if (!blog.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("User does not have permission to add version");
        }

        BlogVersion blogVersion = blogVersionMapper.fromRequest(versionRequest, blog, LocalDateTime.now());
        BlogVersion savedBlogVersion = blogVersionRepository.save(blogVersion);
        return blogVersionMapper.toResponse(savedBlogVersion);
    }

    @Override
    public List<BlogVersionResponse> getAllVersions(Long blogId, String userEmail) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IdNotFoundException("Blog Id not found"));
        if (!blog.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("User does not have permission to get the versions of this blog");
        }
        return blogVersionRepository
                .findAllByBlogId(blogId)
                .stream()
                .map(blogVersionMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteVersion(Long versionId, Long blogId, String userEmail) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IdNotFoundException("Blog Id not found"));
        if (!blog.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("User does not have permission to delete a version of this blog");
        }
        blogVersionRepository.deleteById(versionId);
    }
}
