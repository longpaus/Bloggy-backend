package com.bloggy.blogVersion;

import com.bloggy.blog.Blog;
import com.bloggy.blog.dto.BlogRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface IBlogVersionMapper {
    /**
     * Maps a BlogVersionRequest to BlogVersion.
     *
     * @param versionRequest the request object
     * @param blog           the blog object of the blogVersion
     * @param time           the time this version was created
     * @return the mapped BlogVersion object
     */
    @Mapping(target = "blog", source = "blog")
    @Mapping(target = "time", source = "time")
    BlogVersion blogVersionRequestToBlogVersion(BlogVersionRequest versionRequest, Blog blog, LocalDateTime time);

    @Mapping(target = "blog", source = "blog")
    @Mapping(target = "time", source = "time")
    BlogVersion blogRequestToBlogVersion(BlogRequest request, Blog blog, LocalDateTime time);

    @Mapping(target = "time", source = "time", dateFormat = "mm.HH.dd.MM.yyyy")
    BlogVersionResponse blogVersionToResponse(BlogVersion blogVersion);

}
