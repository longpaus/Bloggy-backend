package com.bloggy.blog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogMetaData {
    private Long id;
    private String title;
    private String description;
    private String username;
}
