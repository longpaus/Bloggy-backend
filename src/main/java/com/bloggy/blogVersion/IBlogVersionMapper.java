package com.bloggy.blogVersion;

import com.bloggy.blog.Blog;
import com.bloggy.blog.dto.BlogRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface IBlogVersionMapper {
    @Mapping(target = "blog", source = "blog")
    @Mapping(target = "time", source = "time")
    BlogVersion fromRequest(BlogVersionRequest versionRequest, Blog blog, LocalDateTime time);

    @Mapping(target = "blog", source = "blog")
    @Mapping(target = "time", source = "time")
    BlogVersion blogRequestToBlogVersion(BlogRequest request, Blog blog, LocalDateTime time);

    @Mapping(target = "time", source = "time", dateFormat = "mm.HH.dd.MM.yyyy")
    BlogVersionResponse blogVersionToResponse(BlogVersion blogVersion);

    @Mapping(target = "time", source = "time", dateFormat = "mm.HH.dd.MM.yyyy")
    BlogVersionResponse toResponse(BlogVersion blogVersion);
}
