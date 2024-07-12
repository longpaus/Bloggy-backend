package com.bloggy.blog;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogRequest {
    private String title;
    private String content;
}
