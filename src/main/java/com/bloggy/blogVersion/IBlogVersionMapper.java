package com.bloggy.blogVersion;

import com.bloggy.blog.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IBlogVersionMapper {

    @Mapping(target = "blog", source = "blog")
    BlogVersion fromRequest(BlogVersionRequest versionRequest, Blog blog);

    @Mapping(target = "blogId", expression = "java(savedBlogVersion.getBlog().getId())")
    BlogVersionResponse toResponse(BlogVersion savedBlogVersion);
}
