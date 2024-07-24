package com.bloggy.publishedBlog;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishedBlogRequest {
    @NotNull(message = "blog must be stated as private or public")
    private boolean isPublic;

    @NotNull
    @Size(min = 1, max = 10000)
    private String Content;
}
