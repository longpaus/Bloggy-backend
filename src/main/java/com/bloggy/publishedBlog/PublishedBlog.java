package com.bloggy.publishedBlog;


import com.bloggy.blog.Blog;
import com.bloggy.blogVersion.BlogVersion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "publised_blogs")
public class PublishedBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "blog_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Blog blog;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "blog_version_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BlogVersion version;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "published_time", nullable = false)
    private LocalDateTime publishedTime;
}
