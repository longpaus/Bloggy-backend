package com.bloggy.blogVersion;

import com.bloggy.blog.Blog;
import com.bloggy.blog.IBlogRepository;
import com.bloggy.user.IUserRepository;
import com.bloggy.user.Role;
import com.bloggy.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")  // Use a different profile for tests if needed
public class BlogVersionRepositoryTest {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IBlogRepository blogRepository;

    @Autowired
    private IBlogVersionRepository blogVersionRepository;

    @Test
    public void testFindAllBlogVersions() {
        LocalDateTime firstSavedTime = LocalDateTime.now();
        LocalDateTime secondSavedTime = LocalDateTime.now().plusMinutes(1);
        LocalDateTime thirdSavedTime = LocalDateTime.now().plusMinutes(2);
        //arrange
        User user1 = User.builder()
                .email("longpaus@gmail.com")
                .role(Role.USER)
                .password("password")
                .build();
        User savedUser1 = userRepository.save(user1);

        Blog blog = Blog.builder()
                .user(savedUser1)
                .title("Long's first blog")
                .build();
        Blog savedBlog = blogRepository.save(blog);

        Blog otherBlog = Blog.builder()
                .user(savedUser1)
                .title("Long's 2nd blog")
                .build();
        blogRepository.save(otherBlog);

        BlogVersion version1 = BlogVersion.builder()
                .blog(savedBlog)
                .content("initial content")
                .time(firstSavedTime)
                .build();
        BlogVersion savedVersion1 = blogVersionRepository.save(version1);

        BlogVersion version2 = BlogVersion.builder()
                .blog(savedBlog)
                .content("Hey, my name is Long")
                .time(secondSavedTime)
                .build();
        BlogVersion savedVersion2 = blogVersionRepository.save(version2);

        BlogVersion version3 = BlogVersion.builder()
                .blog(savedBlog)
                .content("Hey my name is Long, very nice to meet you all!")
                .time(thirdSavedTime)
                .build();
        BlogVersion savedVersion3 = blogVersionRepository.save(version3);


        // act
        List<BlogVersion> versions = blogVersionRepository.findAllByBlogId(blog.getId());

        // assert
        assertEquals(3, versions.size());
        assertEquals(savedVersion1, versions.get(0));
        assertEquals(savedVersion2, versions.get(1));
        assertEquals(savedVersion3, versions.get(2));

    }
}
