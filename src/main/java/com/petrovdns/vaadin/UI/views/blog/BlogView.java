package com.petrovdns.vaadin.UI.views.blog;

import com.petrovdns.vaadin.UI.views.navbar.MainLayout;
import com.petrovdns.vaadin.data.entity.BlogPost;
import com.petrovdns.vaadin.data.service.BlogService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class BlogView extends VerticalLayout {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    TextField filterText = new TextField();

    @Autowired
    public BlogView(BlogService blogService) {
        List<BlogPost> blogPosts = blogService.findAllBlogPost(filterText.getValue());

        FlexLayout blogLayout = new FlexLayout();
        blogLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
        blogLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        blogLayout.setWidthFull();

        Button addPostButton = new Button("Write new post");
        addPostButton.addClickListener(e -> addNewPost());
        addPostButton.addClassNames("btn btn-outline-primary");
        addPostButton.getElement().getStyle().set("margin-left", "16px");

        for (BlogPost post : blogPosts) {
            blogLayout.add(createBlogList(post));
        }

        add(addPostButton, blogLayout);
    }

    private void addNewPost() {
        UI.getCurrent().navigate(BlogPostWrite.class);
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
