package com.petrovdns.vaadin.UI.views.blog;

import com.petrovdns.vaadin.UI.views.navbar.MainLayout;
import com.petrovdns.vaadin.config.security.SecurityService;
import com.petrovdns.vaadin.config.security.SecurityUtil;
import com.petrovdns.vaadin.data.entity.BlogPost;
import com.petrovdns.vaadin.data.service.BlogService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@PageTitle("Blog | Petrov BLOG")
@Route(value = "my-blog-post", layout = MainLayout.class)
@PermitAll
public class OwnerBlogPostView extends VerticalLayout {

    @Value("${app.file.upload-dir}")
    private String uploadDir;



    @Autowired
    public OwnerBlogPostView(BlogService blogService, SecurityUtil securityUtil) {
        List<BlogPost> blogPosts = blogService.findBlogPostByAuthor(securityUtil.getCurrentUserId());

        FlexLayout blogLayout = new FlexLayout();
        blogLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
        blogLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        blogLayout.setWidthFull();

        for (BlogPost post : blogPosts) {
            blogLayout.add(createBlogList(post));
        }

        add(blogLayout);
    }

    private VerticalLayout createBlogList(BlogPost post) {
        VerticalLayout postLayout = new VerticalLayout();

        StreamResource resource = new StreamResource(post.getImage(), () -> {
            try {
                return new FileInputStream(new File(uploadDir, post.getImage()));
            } catch (FileNotFoundException e) {
                Notification.show("File not found: " + e.getMessage());
                return null;
            }
        });

        Image image = new Image(resource, "");
        // Image image = new Image("./uploads/" + post.getImage(), "");
        image.setClassName("rounded-image");
        image.setWidth("373.33px");
        image.setHeight("209.98px");

        Anchor imageLink = new Anchor("/blog/" + post.getId(), image);
        imageLink.setClassName("block");

        H3 title = new H3(post.getTitle());
        title.addClassNames("custom-title-blog", "limited-title");

        Anchor titleLink = new Anchor("/blog/" + post.getId(), title);
        titleLink.setClassName("custom-anchor");

        Div description = new Div();
        description.setText(post.getContent().replaceAll("<[^>]*>", ""));
        description.addClassNames("limited-text");

        postLayout.add(imageLink, titleLink, description);
        postLayout.setAlignItems(Alignment.CENTER);
        postLayout.setWidth("404px");
        postLayout.setHeight(null);
        postLayout.setPadding(false);
        postLayout.add(new Html("<br>"));

        return postLayout;
    }
}
