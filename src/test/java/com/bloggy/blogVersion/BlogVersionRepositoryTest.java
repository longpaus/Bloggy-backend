package com.bloggy.blogVersion;

import com.bloggy.blog.Blog;
import com.bloggy.blog.IBlogRepository;
import com.bloggy.user.IUserRepository;
import com.bloggy.user.Role;
import com.bloggy.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BlogVersionRepositoryTest {
    @Autowired
    IBlogVersionRepository blogVersionRepository;

    @Autowired
    IBlogRepository blogRepository;

    @Autowired
    IUserRepository userRepository;

    @Test
    public void testFindByBlogId() {
        // arrange
        User user = userRepository.save(User.builder()
                .password("password")
                .role(Role.USER)
                .email("longpaus@gmail.com")
                .userName("long")
                .build()
        );
        Blog blog = blogRepository.save(Blog.builder()
                .title("blog title")
                .user(user)
                .build()
        );
        String[] contents = {"content 1", "content 2", "content 3", "content 4", "content 5", "content 6", "content 7", "content 8", "content 9", "content 10"};

        for (String content : contents) {
            blogVersionRepository.save(BlogVersion.builder()
                    .blog(blog)
                    .content(content)
                    .time(LocalDateTime.now())
                    .build()
            );
        }
        // act
        int pageSize = 3;
        List<BlogVersion> firstPage = blogVersionRepository.findByBlogId(blog.getId(), PageRequest.of(0, pageSize));
        List<BlogVersion> lastPage = blogVersionRepository.findByBlogId(blog.getId(), PageRequest.of(3, pageSize));
        List<BlogVersion> nonExistantPage = blogVersionRepository.findByBlogId(blog.getId(), PageRequest.of(10, pageSize));
        // assert
        assertEquals(pageSize, firstPage.size());
        assertEquals(contents[0], firstPage.getFirst().getContent());

        assertEquals(1, lastPage.size());
        assertEquals(contents[9], lastPage.getFirst().getContent());

        assertEquals(0, nonExistantPage.size());
    }

    @Test
    public void testFindLatestBlogVersionsByUserId() {
        // arrange
        User user = userRepository.save(User.builder()
                .password("password")
                .role(Role.USER)
                .email("longpaus@gmail.com")
                .userName("long")
                .build()
        );
        String[] blog1Contents = {"content 1", "content 2", "content 3", "content 4"};
        generateBlogAndContents(user, "blog 1", blog1Contents);
        String[] blog2Contents = {"content 1", "content 2", "content 3", "contentttttttttt"};
        generateBlogAndContents(user, "blog 2", blog2Contents);
        String[] blog3Contents = {"content 8", "content 2", "ntent 3", ""};
        generateBlogAndContents(user, "blog 3", blog3Contents);
        String[] blog4Contents = {"content 8", "content 2", "ntent kdjfrksjfk 4", "kkknnnn iiienjkdfn"};
        generateBlogAndContents(user, "blog 4", blog4Contents);
        String[] blog5Contents = {"content 8"};
        generateBlogAndContents(user, "blog 5", blog5Contents);

        // act
        int pageSize = 2;
        List<BlogVersion> firstPage = blogVersionRepository.findLatestBlogVersionsByUserId(user.getId(), PageRequest.of(0, pageSize));
        List<BlogVersion> secondPage = blogVersionRepository.findLatestBlogVersionsByUserId(user.getId(), PageRequest.of(1, pageSize));
        List<BlogVersion> lastPage = blogVersionRepository.findLatestBlogVersionsByUserId(user.getId(), PageRequest.of(2, pageSize));

        // assert
        assertEquals(pageSize, firstPage.size());
        assertEquals(blog1Contents[blog1Contents.length - 1], firstPage.getFirst().getContent());
        assertEquals(blog2Contents[blog2Contents.length - 1], firstPage.get(1).getContent());
        assertEquals(blog3Contents[blog3Contents.length - 1], secondPage.getFirst().getContent());
        assertEquals(1, lastPage.size());
        assertEquals(blog5Contents[blog5Contents.length - 1], lastPage.getFirst().getContent());
    }

    @Test
    public void testFindFirstByBlogOrderByTimeDesc() {
        // arrange
        User user = userRepository.save(User.builder()
                .password("password")
                .role(Role.USER)
                .email("longpaus@gmail.com")
                .userName("long")
                .build()
        );
        Blog blog = blogRepository.save(Blog.builder()
                .title("blog title")
                .user(user)
                .build()
        );
        String[] contents = {"content 1", "content 2", "content 3", "content 4", "content 5", "content 6", "content 7", "content 8", "content 9", "content 10"};

        for (String content : contents) {
            blogVersionRepository.save(BlogVersion.builder()
                    .blog(blog)
                    .content(content)
                    .time(LocalDateTime.now())
                    .build()
            );
        }
        // act
        Optional<BlogVersion> opt = blogVersionRepository.findFirstByBlogOrderByTimeDesc(blog.getId());

        assertTrue(opt.isPresent());
        BlogVersion version = opt.get();
        // assert
        assertEquals(contents[contents.length - 1], version.getContent());
    }

    private void generateBlogAndContents(User user, String blogTitle, String[] contents) {
        Blog blog = blogRepository.save(Blog.builder()
                .title(blogTitle)
                .user(user)
                .build()
        );
        for (String content : contents) {
            blogVersionRepository.save(BlogVersion.builder()
                    .blog(blog)
                    .content(content)
                    .time(LocalDateTime.now())
                    .build()
            );
        }
    }
}
