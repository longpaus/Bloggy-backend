package com.bloggy.blogVersion;

import com.bloggy.blog.Blog;
import com.bloggy.user.Role;
import com.bloggy.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BlogVersionMapperTest {

    @Autowired
    private IBlogVersionMapper blogVersionMapper;

    @Test
    public void testFromRequest() {
        // arrange
        LocalDateTime today = LocalDateTime.now();
        User user = User.builder()
                .email("longpaus@gmail.com")
                .role(Role.USER)
                .password("password")
                .id(1L)
                .build();
        Blog blog = Blog.builder()
                .id(1L)
                .user(user)
                .title("blog 1")
                .build();
        BlogVersionRequest request = BlogVersionRequest
                .builder()
                .content("content of blog 1")
                .build();

        BlogVersion expected = BlogVersion.builder()
                .content(request.getContent())
                .blog(blog)
                .time(today)
                .id(1L)
                .build();
        // act
        BlogVersion blogVersion = blogVersionMapper.fromRequest(request, blog, today);

        // assert
        assertNotNull(blogVersion);
        assertEquals(expected, blogVersion);
    }

    @Test
    public void testToResponse() {
        int year = 2024;
        int month = 9;
        int day = 21;
        int hr = 4;
        int min = 11;
        int sec = 38;
        // arrange
        LocalDate date = LocalDate.of(year, month, day);
        LocalTime time = LocalTime.of(hr, min, sec);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        User user = User.builder()
                .email("longpaus@gmail.com")
                .role(Role.USER)
                .password("password")
                .id(1L)
                .build();
        Blog blog = Blog.builder()
                .id(1L)
                .user(user)
                .title("blog 1")
                .build();

        BlogVersion blogVersion = BlogVersion.builder()
                .id(1L)
                .blog(blog)
                .content("content of blog version")
                .time(dateTime)
                .build();

        // Expected time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm.HH.dd.MM.yyyy");
        String formattedTime = dateTime.format(formatter);

        BlogVersionResponse expected = BlogVersionResponse.builder()
                .id(blogVersion.getId())
                .content(blogVersion.getContent())
                .time(formattedTime)
                .build();
        // act
        BlogVersionResponse response = blogVersionMapper.toResponse(blogVersion);
        assertNotNull(response);
        assertEquals(expected, response);
    }
}
