package com.bloggy.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findById(Long id);

    @Query(value = "Select * From blogs b where b.user_id = ?1", nativeQuery = true)
    List<Blog> findAllByUserId(Long userId);
}
