package com.bloggy.blog;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogResponse {
    private String title;
    private String content;
    private boolean isPublic;
    private long id;
}
