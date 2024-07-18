package com.bloggy.blogVersion;

import com.bloggy.blog.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface IBlogVersionMapper {
    @Mapping(target = "blog", source = "blog")
    @Mapping(target = "time", source = "time")
    BlogVersion fromRequest(BlogVersionRequest versionRequest, Blog blog, LocalDateTime time);

    @Mapping(target = "blogId", expression = "java(blogVersion.getBlog().getId())")
    @Mapping(target = "time", source = "time", dateFormat = "mm.HH.dd.MM.yyyy")
    BlogVersionResponse toResponse(BlogVersion blogVersion);
}
