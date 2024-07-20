package com.bloggy.blogVersion;

import java.util.List;

public interface IBlogVersionService {

    BlogVersionResponse addVersion(BlogVersionRequest version, Long blogId, String userEmail);

    List<BlogVersionResponse> getAllVersions(Long blogId, String userEmail);

    void deleteVersion(Long versionId, Long blogId, String userEmail);
}
