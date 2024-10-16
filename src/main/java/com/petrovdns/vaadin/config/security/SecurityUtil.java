package com.petrovdns.vaadin.config.security;

import com.petrovdns.vaadin.data.entity.BlogPost;
import com.petrovdns.vaadin.data.entity.UserEntity;
import com.petrovdns.vaadin.data.enums.Role;
import com.petrovdns.vaadin.data.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@Component
public class SecurityUtil {

    @Autowired
    SecurityService securityService;
    @Autowired
    BlogService blogService;

    public boolean isUserAdmin() {
        String currentUsername = securityService.getAuthenticatedUser().getUsername();
        UserEntity currentUser = blogService.findUserByUserName(currentUsername);
        return currentUser.getRoles().stream().anyMatch(e -> e.getRoleName().equals(Role.ADMIN.name()));
    }

    public boolean isUserRegular() {
        String currentUsername = securityService.getAuthenticatedUser().getUsername();
        UserEntity currentUser = blogService.findUserByUserName(currentUsername);
        return currentUser.getRoles().stream().anyMatch(e -> e.getRoleName().equals(Role.USER.name()));
    }

    public boolean isPostOwnerOrAdmin(BlogPost blogPost) {
        String currentUsername = securityService.getAuthenticatedUser().getUsername();
        if (currentUsername != null && blogPost != null) {
            return Objects.equals(blogPost.getAuthor().getUsername(), currentUsername) || isUserAdmin();
        }
        return false;
    }

    public UserEntity getCurrentUser() {
        String currentUsername = securityService.getAuthenticatedUser().getUsername();
        return blogService.findUserByUserName(currentUsername);
    }

    public Long getCurrentUserId() {
        String currentUsername = securityService.getAuthenticatedUser().getUsername();
        return blogService.findUserByUserName(currentUsername).getId();
    }
}
