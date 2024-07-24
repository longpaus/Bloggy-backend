package com.bloggy.publishedBlog;

import lombok.Data;

@Data
public class PublishedBlogResponse {
    private Long id;
    private boolean isPublished;
    private String publishedTime;
}
