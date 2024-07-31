package com.bloggy.publishedBlog;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishedBlogRequest {
    @NotNull(message = "blog must be stated as private or public")
    private boolean isPublic;

    @NotNull(message = "The versionId can't be null")
    private Long versionId;
}
