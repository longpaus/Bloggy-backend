package com.bloggy.blogVersion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogVersionResponse {
    private Long id;
    private String title;
    private String content;
    private String time;
}
