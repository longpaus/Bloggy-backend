package com.bloggy.blogVersion;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IBlogVersionRepository extends JpaRepository<BlogVersion, Long> {
    @Query(nativeQuery = true, value = "Select * From blog_versions Where blog_id = ?1")
    List<BlogVersion> findByBlogId(Long blogId, Pageable pageable);

    @Query("SELECT bv FROM BlogVersion bv " +
            "WHERE bv.blog.user.id = :userId AND bv.time = (" +
            "SELECT MAX(bv2.time) FROM BlogVersion bv2 " +
            "WHERE bv2.blog.id = bv.blog.id)")
    List<BlogVersion> findLatestBlogVersionsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM blog_versions WHERE blog_id = ?1 ORDER BY time DESC LIMIT 1", nativeQuery = true)
    Optional<BlogVersion> findFirstByBlogOrderByTimeDesc(Long blogId);
}