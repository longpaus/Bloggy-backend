package com.bloggy.publishedBlog;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
