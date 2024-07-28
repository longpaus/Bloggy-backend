package com.bloggy.blog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogResponse {
    private String title;
    private String description;
    private String content;
    private String username;
}
