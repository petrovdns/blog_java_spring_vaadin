package com.petrovdns.vaadin.data.entity;

import com.petrovdns.vaadin.data.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@Entity
@Table(name = "blogposts")
public class BlogPost extends AbstractEntity {

    //@NotNull
    @Column(name = "image_link")
    private String image;

    @NotEmpty(message = "Write a title please")
    @Size(min = 5, message = "Write minim 5 characters")
    private String title = "";

    @NotEmpty(message = "Write content please")
    @Size(min = 20, message = "Write minim 20 characters")
    private String content = "";

    //@NotEmpty
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private UserEntity author;

    public BlogPost() {
    }

    public BlogPost(String image, String title, String content, UserEntity author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.image = image;
    }

    public String getImage() {
        if (image == null || image.isEmpty()) {
            return "blog_post_default.jpg";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
