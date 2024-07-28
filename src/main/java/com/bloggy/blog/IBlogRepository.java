package com.bloggy.blog;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBlogRepository extends JpaRepository<Blog, Long> {

    @Query(value = "Select * From blogs Where blogs.user_id = ?1", nativeQuery = true)
    List<Blog> findAllByUserId(Long userId, Pageable pageable);
}
