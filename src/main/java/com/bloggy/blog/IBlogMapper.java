package com.bloggy.blog;

import com.bloggy.blog.dto.BlogMetaData;
import com.bloggy.blog.dto.BlogRequest;
import com.bloggy.blog.dto.BlogResponse;
import com.bloggy.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IBlogMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    Blog BlogReqestToBlog(BlogRequest blogRequest, User user);

    @Mapping(target = "content", source = "content")
    @Mapping(target = "username", expression = "java(user.getUserName())")
    BlogResponse blogToBlogResponse(Blog blog, String content, User user);

    @Mapping(target = "username", expression = "java(blog.getUser().getUserName())")
    BlogMetaData blogToBlogMetaData(Blog blog);
}
