package com.petrovdns.vaadin.UI.views.blog;

import com.petrovdns.vaadin.UI.views.navbar.MainLayout;
import com.petrovdns.vaadin.config.security.SecurityUtil;
import com.petrovdns.vaadin.data.entity.BlogPost;
import com.petrovdns.vaadin.data.entity.UserEntity;
import com.petrovdns.vaadin.data.service.BlogService;
import com.petrovdns.vaadin.config.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.io.*;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@PageTitle("Write | Petrov BLOG")
@Route(value = "new-post", layout = MainLayout.class)
@PreserveOnRefresh
@PermitAll
public class BlogPostWrite extends VerticalLayout {
    Binder<BlogPost> binder = new BeanValidationBinder<>(BlogPost.class);
    BlogPost blogPost;

    private BlogService blogService;
    private SecurityService securityService;
    private SecurityUtil securityUtil;

    TextField id;
    TextField image;
    TextField title = new TextField("Title");
    RichTextEditor content = new RichTextEditor();
    TextField version;

    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    @Value("${app.file.upload-dir}")
    private String uploadDir;

    VerticalLayout verticalLayout;

    @Autowired
    public BlogPostWrite(BlogService blogService, SecurityService securityService, SecurityUtil securityUtil) {
        this.blogService = blogService;
        this.securityService = securityService;
        this.securityUtil = securityUtil;
        this.blogPost = blogService.getCurrentBlogPost(); //for editing //default: null
        blogService.removeCurrentBlogPost();
        addSaveListener(this::saveBlogPost);
        createBlogPostWritePage();
    }

    private void createBlogPostWritePage() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        binder.bindInstanceFields(this);
        setSizeFull();

        version.setValue("1");

        title.setWidth("98%");
        title.getElement().getStyle().set("margin-left", "16px");

        content.setWidth("98%");
        content.getElement().getStyle().set("margin-left", "16px");

        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setAutoUpload(true);
        upload.setMaxFileSize(5 * 1024 * 1024);
        upload.getElement().getStyle().set("margin-left", "16px");
        upload.setDropLabel(new Span(".jpg, .png, .gif - Size: 1600x900"));
        upload.addSucceededListener(event -> {
//            setNameImage(event.getFileName());
            this.image.setValue(event.getFileName());

            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                boolean mkdirs = uploadDirectory.mkdirs();
                if (mkdirs) Notification.show("Directory has been created!");
            }

            File file = new File(uploadDirectory, image.getValue());
            try (InputStream inputStream = buffer.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(file)) {

                inputStream.transferTo(outputStream);

                Notification.show(image.getValue() + " has been uploaded!");
                if (blogPost != null) removeImageBlock(horizontalLayout);
                showImageBlock(horizontalLayout, file);

            } catch (Exception e) {
                Notification.show("Upload error: " + e.getMessage());
            }
        });

        upload.setMaxFiles(1);
        upload.addFileRemovedListener(event -> {
            removeImageBlock(horizontalLayout);
            if (blogPost != null) {
                image.setValue(blogPost.getImage());
                showImageBlock(horizontalLayout, null);
                Notification.show(blogPost.getImage() + " restored!");
            }
        });

        if (blogPost != null) {
            id.setValue(blogPost.getId().toString());
            image.setValue(blogPost.getImage());
            title.setValue(blogPost.getTitle());
            content.setValue(blogPost.getContent());
            version.setValue(Integer.toString(blogPost.getVersion()));
            //UI.getCurrent().getPage().setLocation("edit-post/" + blogPost.getId());
        }

        horizontalLayout.setSpacing(true);
        horizontalLayout.add(upload);

        add(addBackButton(), horizontalLayout, title, content, addSaveButton());
        if (blogPost != null) showImageBlock(horizontalLayout, null);
    }

    private void showImageBlock(HorizontalLayout horizontalLayout, File file) {
        StreamResource resource;
        verticalLayout = new VerticalLayout();

        if (file != null) {
            resource = new StreamResource(file.getName(), () -> {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    Notification.show("File not found: " + e.getMessage());
                    return null;
                }
            });
        } else {
            resource = new StreamResource(blogPost.getImage(), () -> {
                try {
                    return new FileInputStream(new File(uploadDir, blogPost.getImage()));
                } catch (FileNotFoundException e) {
                    Notification.show("File not found: " + e.getMessage());
                    return null;
                }
            });
        }

        Image imageLink = new Image(resource, "Image");
        imageLink.addClassNames("rounded-image", "no-drag");
        imageLink.setWidth("230.1px");
        imageLink.setHeight("130px");


        verticalLayout.add(imageLink);
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setWidth("230.1px");
        verticalLayout.setHeight(null);
        verticalLayout.setPadding(false);
        horizontalLayout.add(verticalLayout);
    }

    private void removeImageBlock(HorizontalLayout horizontalLayout) {
        horizontalLayout.remove(verticalLayout);
    }

    private Component addSaveButton() {
        Button addSaveButton = new Button("Save");
        addSaveButton.addClickListener(e -> validateAndSave());
        addSaveButton.addClassNames("btn btn-outline-success");
        addSaveButton.getElement().getStyle().set("margin-left", "16px");
        return addSaveButton;
    }

    private Component addBackButton() {
        Button addBackButton = new Button("<- back");
        addBackButton.addClickListener(e -> backPage());
        addBackButton.addClassNames("btn btn-outline-primary");
        addBackButton.getElement().getStyle().set("margin-left", "16px");
        return addBackButton;
    }

    private void backPage() {
        UI.getCurrent().navigate(BlogView.class);
    }

    private void saveBlogPost(SaveEvent event) {
        try {
            UserEntity currentUser = securityUtil.getCurrentUser();
            BlogPost post = event.getBlogPost();
            if(blogPost == null) {
                post.setAuthor(currentUser);
            } else {
                post.setAuthor(blogPost.getAuthor());
            }
            blogService.saveBlogPost(post);
            if (blogPost != null) {
                Notification.show(title.getValue() + " has been updated!");
            } else {
                Notification.show(title.getValue() + " has been published!");
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            Notification.show("Save Error!");
        }
    }

    private void validateAndSave() {
        try {
            BlogPost post = new BlogPost();
            binder.writeBean(post);
            fireEvent(new SaveEvent(this, post));
            backPage();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //SaveEvent
    public static abstract class BlogSaveEvent extends ComponentEvent<BlogPostWrite> {
        private final BlogPost blogPost;

        protected BlogSaveEvent(BlogPostWrite source, BlogPost blogPost) {
            super(source, false);
            this.blogPost = blogPost;
        }

        public BlogPost getBlogPost() {
            return blogPost;
        }
    }

    public static class SaveEvent extends BlogSaveEvent {
        public SaveEvent(BlogPostWrite source, BlogPost blogPost) {
            super(source, blogPost);
        }

    }

    public void addSaveListener(ComponentEventListener<BlogPostWrite.SaveEvent> listener) {
        addListener(BlogPostWrite.SaveEvent.class, listener);
    }
}
