package com.bloggy.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogRequest {
    @NotBlank(message = "title can't be blank")
    private String title;

    private String content;

    private String description;
}
