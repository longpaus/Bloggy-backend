package com.bloggy.blog;

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
    private final BlogMapper blogMapper;

    @Override
    public BlogResponse createBlog(BlogRequest newBlog, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user email not found"));
        Blog blog = blogMapper.BlogReqestToBlog(newBlog,user);
        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.BlogToBlogResponse(savedBlog);
    }

    @Override
    public BlogResponse updateBlog(BlogRequest updatedBlog, Long blogId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user email not found"));
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("blog not found"));
        if(!user.getId().equals(blog.getUser().getId())) {
            throw new RuntimeException("user id mismatch");
        }
        Blog newBlog = blogMapper.BlogReqestToBlog(updatedBlog, user);
        return blogMapper.BlogToBlogResponse(blogRepository.save(newBlog));
    }

    @Override
    public void deleteBlog(Long blogId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user email not found"));
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("blog not found"));
        if(!user.getId().equals(blog.getUser().getId())) {
            throw new RuntimeException("user id mismatch");
        }
        blogRepository.delete(blog);
    }

    @Override
    public List<BlogResponse> getAllBlogs(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user email not found"));
        return blogRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(blogMapper::BlogToBlogResponse)
                .toList();
    }
}
