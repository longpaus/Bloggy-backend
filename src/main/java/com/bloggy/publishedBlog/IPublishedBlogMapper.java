package com.bloggy.publishedBlog;

import com.bloggy.blog.Blog;
import com.bloggy.blogVersion.BlogVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface IPublishedBlogMapper {

    @Mapping(target = "blog", source = "blog")
    @Mapping(target = "version", source = "blogVersion")
    @Mapping(target = "publishedTime", source = "time")
    @Mapping(target = "id", ignore = true)
    PublishedBlog fromRequest(PublishedBlogRequest publishedBlogRequest, Blog blog, BlogVersion blogVersion, LocalDateTime time);

    @Mapping(target = "publishedTime", source = "publishedTime", dateFormat = "mm.HH.dd.MM.yyyy")
    PublishedBlogResponse toResponse(PublishedBlog publishedBlog);
}
