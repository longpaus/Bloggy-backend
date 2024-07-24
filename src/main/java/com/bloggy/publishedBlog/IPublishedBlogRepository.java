package com.bloggy.publishedBlog;


import org.springframework.data.jpa.repository.JpaRepository;

public interface IPublishedBlogRepository extends JpaRepository<PublishedBlog, Long> {

}
