package com.bloggy.blog;

import com.bloggy.exception.IdNotFoundException;
import com.bloggy.exception.UnauthorizedException;
import com.bloggy.user.IUserRepository;
import com.bloggy.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BlogService implements IBlogService {

    private final IBlogRepository blogRepository;
    private final IUserRepository userRepository;
    private final IBlogMapper blogMapper;

    @Override
    public BlogResponse createBlog(BlogRequest newBlog, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IdNotFoundException("user email not found"));
        Blog blog = blogMapper.BlogReqestToBlog(newBlog, user);
        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.BlogToBlogResponse(savedBlog);
    }

    @Override
    public BlogResponse updateBlog(BlogRequest updatedBlog, Long blogId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IdNotFoundException("user email not found"));
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IdNotFoundException("blog not found"));
        if (!user.getId().equals(blog.getUser().getId())) {
            throw new UnauthorizedException("user not authorized to update this blog");
        }
        Blog newBlog = blogMapper.BlogReqestToBlog(updatedBlog, user);
        return blogMapper.BlogToBlogResponse(blogRepository.save(newBlog));
    }

    @Override
    public void deleteBlog(Long blogId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IdNotFoundException("user email not found"));
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IdNotFoundException("blog not found"));
        if (!user.getId().equals(blog.getUser().getId())) {
            throw new UnauthorizedException("user not authorized to delete blog");
        }
        blogRepository.delete(blog);
    }

    @Override
    public List<BlogResponse> getUserBlogs(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IdNotFoundException("user email not found"));
        return blogRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(blogMapper::BlogToBlogResponse)
                .toList();
    }
}
