package com.petrovdns.vaadin.UI.views.blog;

import com.petrovdns.vaadin.UI.views.navbar.MainLayout;
import com.petrovdns.vaadin.config.security.SecurityUtil;
import com.petrovdns.vaadin.data.entity.BlogPost;
import com.petrovdns.vaadin.data.service.BlogService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@Route(value = "blog/:postID", layout = MainLayout.class)
@PreserveOnRefresh
@PermitAll
public class BlogPostView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

    private final BlogService blogService;
    private final SecurityUtil securityUtil;

    private int postID;
    private String pageTitle;
    private BlogPost blogPost;

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Autowired
    public BlogPostView(BlogService blogService, SecurityUtil securityUtil) {
        this.blogService = blogService;
        this.securityUtil = securityUtil;
        setRouteValue();
        add(addBackButton(), new H1("POST NOT FOUND 404"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> postNumberCheck = event.getRouteParameters().get("postID");
        if (postNumberCheck.isPresent()) {
            String postNumber = postNumberCheck.get();
            if (postNumber.matches("\\d+")) {
                setPostID(Integer.parseInt(postNumber));
                BlogPost post = blogService.findBlogPostById(postID);
                if (post != null) {
                    createBlogPostLayout(post);
                }
            }
        }
    }

    private void createBlogPostLayout(BlogPost post) {
        removeAll();
        blogPost = post;
        addDeleteListener(this::removeBlogPost);
        HorizontalLayout horizontalButtonLayout = new HorizontalLayout();
        setRouteValue(post.getTitle());
        H1 h1 = new H1(post.getTitle());
        //H4 h4 = new H4(post.getContent());
        Div h4 = new Div();
        h4.getElement().setProperty("innerHTML", post.getContent());

        StreamResource resource = new StreamResource(blogPost.getImage(), () -> {
            try {
                return new FileInputStream(new File(uploadDir, blogPost.getImage()));
            } catch (FileNotFoundException e) {
                Notification.show("File not found: " + e.getMessage());
                return null;
            }
        });

        Image image = new Image(resource, "");
        image.addClassNames("rounded-image", "image-size");
//        image.setWidth("50%");
//        image.setHeight("50%");

        VerticalLayout verticalLayoutTitle = new VerticalLayout();
        horizontalButtonLayout.add(addBackButton(), addEditPostButton(), addRemovePostButton());
        verticalLayoutTitle.add(h1);
        verticalLayoutTitle.setSizeFull();
        verticalLayoutTitle.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayoutTitle.setAlignItems(Alignment.CENTER);
        //verticalLayout.getElement().getStyle().set("margin-left", "16px");

        VerticalLayout verticalLayoutImage = new VerticalLayout();
        verticalLayoutImage.setSizeFull();
        verticalLayoutImage.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayoutImage.setAlignItems(Alignment.CENTER);
        verticalLayoutImage.add(image);

        VerticalLayout verticalLayoutContent = new VerticalLayout();
        verticalLayoutContent.setSizeFull();
        verticalLayoutContent.getElement().getStyle().set("margin-left", "12px");
        verticalLayoutContent.add(h4);


        add(horizontalButtonLayout, verticalLayoutTitle, verticalLayoutImage, verticalLayoutContent);
    }

    private void setRouteValue() {
        pageTitle = "404";
    }

    private void setRouteValue(String title) {
        pageTitle = title + " | Petrov BLOG";
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    private Component addBackButton() {
        Button addBackButton = new Button("<- back");
        addBackButton.addClickListener(e -> backPage());
        addBackButton.addClassNames("btn btn-outline-primary");
        addBackButton.getElement().getStyle().set("margin-left", "16px");
        return addBackButton;
    }

    private Component addEditPostButton() {
        Button editPostButton = new Button("Edit");
        editPostButton.addClickListener(this::editBlogPost);
        editPostButton.setVisible(securityUtil.isPostOwnerOrAdmin(blogPost));
        editPostButton.addClassNames("btn btn-outline-warning");
        editPostButton.getElement().getStyle().set("margin-left", "20px");
        return editPostButton;
    }

    private void editBlogPost(ClickEvent<Button> buttonClickEvent) {
        blogService.setCurrentBlogPost(blogPost);
        UI.getCurrent().navigate(BlogPostWrite.class);
    }

    private Component addRemovePostButton() {
        Button removePostButton = new Button("Remove");
        removePostButton.setVisible(securityUtil.isPostOwnerOrAdmin(blogPost));
        removePostButton.addClickListener(event -> fireEvent(new DeleteEvent(this, blogPost)));
        removePostButton.addClassNames("btn btn-outline-danger");
        return removePostButton;
    }

    private void backPage() {
        UI.getCurrent().getPage().getHistory().back();
    }

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    private void removeBlogPost(DeleteEvent event) {
        blogService.deleteBlogPost(event.getBlogPost());
        backPage();
        Notification.show(blogPost.getTitle() + " has been deleted!");
    }


    //DeleteEvent
    public static abstract class BlogDeleteEvent extends ComponentEvent<BlogPostView> {
        private final BlogPost blogPost;

        protected BlogDeleteEvent(BlogPostView source, BlogPost blogPost) {
            super(source, false);
            this.blogPost = blogPost;
        }

        public BlogPost getBlogPost() {
            return blogPost;
        }
    }

    public static class DeleteEvent extends BlogPostView.BlogDeleteEvent {
        public DeleteEvent(BlogPostView source, BlogPost blogPost) {
            super(source, blogPost);
        }

    }

    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(BlogPostView.DeleteEvent.class, listener);
    }
}
