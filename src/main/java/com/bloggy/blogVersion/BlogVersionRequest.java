package com.bloggy.blogVersion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogVersionRequest {
    private Long blogId;
    private String content;
}
