package com.bloggy.blogVersion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBlogVersionRepository extends JpaRepository<BlogVersion, Long> {
    @Query(nativeQuery = true, value = "Select * From blog_versions Where blog_id = ?1")
    List<BlogVersion> findAllByBlogId(Long blogId);
}
