package com.petrovdns.vaadin.data.repository;

import com.petrovdns.vaadin.data.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    @Query("select c from BlogPost c " +
            "where lower(c.title) like lower(concat('%', :searchTerm, '%')) ")
    List<BlogPost> searchBlogByTitle(@Param("searchTerm") String searchTerm);

    @Query("select c from BlogPost c where c.author.id = :id")
    List<BlogPost> searchBlogPostByAuthor(@Param("id") Long id);
}
