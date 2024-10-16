package com.petrovdns.vaadin.UI.views.login;

import com.petrovdns.vaadin.UI.views.blog.BlogView;
import com.petrovdns.vaadin.config.security.SecurityUtil;
import com.petrovdns.vaadin.data.entity.Roles;
import com.petrovdns.vaadin.data.entity.UserEntity;
import com.petrovdns.vaadin.data.repository.RolesRepository;
import com.petrovdns.vaadin.data.service.BlogService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@PageTitle("Register | BLOG Petrov")
@Route("register")
@AnonymousAllowed
public class RegistrationView extends HorizontalLayout {
    Binder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);

    TextField username = new TextField("Username");
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    TextField email = new TextField("Email");
    TextField password = new TextField("Password");
    MultiSelectComboBox<Roles> roles = new MultiSelectComboBox<>("Roles");


    private final BlogService blogService;

    public RegistrationView(BlogService blogService, RolesRepository rolesRepository, SecurityUtil securityUtil) {

        try {
            if (securityUtil.getCurrentUser() != null) {
                UI.getCurrent().access(() -> UI.getCurrent().navigate(BlogView.class));
            }
        }catch (Exception ignored) {
        }

        this.blogService = blogService;
        setSizeFull();

        binder.bindInstanceFields(this);

        roles.setVisible(false);
        List<Roles> role = rolesRepository.findAll().stream().filter(e -> "USER".equals(e.getRoleName())).collect(Collectors.toList());
        roles.setItems(role);
        roles.setItemLabelGenerator(Roles::getRoleName);
        roles.select(role);
        addSaveListener(this::registerUser);

        HorizontalLayout layoutCancelButton = new HorizontalLayout();
        Button addCancelButton = new Button("<-");
        addCancelButton.addClickShortcut(Key.ESCAPE);
        addCancelButton.addClickListener(e -> backToLogin());
        layoutCancelButton.add(addCancelButton);
        layoutCancelButton.setJustifyContentMode(JustifyContentMode.BETWEEN);
        layoutCancelButton.getElement().getStyle().set("position", "absolute");
        layoutCancelButton.getElement().getStyle().set("top", "5px"); // ajustează poziția
        layoutCancelButton.getElement().getStyle().set("left", "5px");
        layoutCancelButton.setWidth(null);
        layoutCancelButton.setHeight(null);

        add(layoutCancelButton, registrationForm());
    }

    private Component registrationForm() {
        VerticalLayout registrationForm = new VerticalLayout();

        registrationForm.setSizeFull();

        Text login = new Text("Login");
        Anchor loginLink = new Anchor("/login", login);

        getStyle().set("position", "fixed");
        getStyle().set("top", "0");
        getStyle().setHeight("100%");
        getStyle().setWidth("100%");

        H2 text = new H2("Registration | BLOG");

        username.setSizeFull();
        firstName.setSizeFull();
        lastName.setSizeFull();
        email.setSizeFull();
        password.setSizeFull();

        registrationForm.setAlignItems(Alignment.CENTER);
        registrationForm.setJustifyContentMode(JustifyContentMode.CENTER);
        registrationForm.setFlexGrow(1);
        registrationForm.setWidth("25em");
        registrationForm.setHeight(null);
        registrationForm.getStyle().set("background-color", "white");
        registrationForm.add(text, username, firstName, lastName, email, password, addButtonRegister());

        VerticalLayout parentLayout = new VerticalLayout();
        parentLayout.setSizeFull();
        parentLayout.setAlignItems(Alignment.CENTER);
        parentLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        parentLayout.add(registrationForm, loginLink);

        return parentLayout;
    }

    private Component addButtonRegister() {
        Button addSaveButton = new Button("Register");
        addSaveButton.addClickListener(e -> validateAndSave());
        addSaveButton.setWidth("50%");
        addSaveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addSaveButton.addClickShortcut(Key.ENTER);
        return addSaveButton;
    }

    private void backToLogin() {
        UI.getCurrent().access(() -> UI.getCurrent().navigate(LoginView.class));
    }

    private void validateAndSave() {
        try {
            UserEntity user = new UserEntity();
            binder.writeBean(user);
            user.setPassword("{noop}" + password.getValue());
            fireEvent(new RegistrationView.SaveEvent(this, user));
            UI.getCurrent().navigate(LoginView.class);
            Notification.show("Registration successful");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(SaveEvent event) {
        blogService.registerUser(event.getContact());
    }

    public static abstract class RegistrationFormEvent extends ComponentEvent<RegistrationView> {
        private final UserEntity userEntity;

        protected RegistrationFormEvent(RegistrationView source, UserEntity userEntity) {
            super(source, false);
            this.userEntity = userEntity;
        }

        public UserEntity getContact() {
            return userEntity;
        }
    }

    public static class SaveEvent extends RegistrationView.RegistrationFormEvent {
        public SaveEvent(RegistrationView source, UserEntity userEntity) {
            super(source, userEntity);
        }
    }

    public void addSaveListener(ComponentEventListener<RegistrationView.SaveEvent> listener) {
        addListener(RegistrationView.SaveEvent.class, listener);
    }
}
