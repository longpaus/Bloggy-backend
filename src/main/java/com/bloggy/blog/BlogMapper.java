package com.bloggy.blog;

import com.bloggy.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BlogMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    Blog BlogReqestToBlog(BlogRequest blogRequest, User user);

    BlogResponse BlogToBlogResponse(Blog blog);
}
