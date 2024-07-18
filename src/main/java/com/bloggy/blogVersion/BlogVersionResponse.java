package com.bloggy.blogVersion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class BlogVersionResponse {
    private Long id;
    private Long blogId;
    private String content;
    private String time;
}
