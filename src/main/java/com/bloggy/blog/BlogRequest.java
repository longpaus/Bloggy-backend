package com.bloggy.blog;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogRequest {
    @NotBlank(message = "title can't be blank")
    private String title;
}
