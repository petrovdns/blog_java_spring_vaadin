package com.petrovdns.vaadin.UI.views.admin;


import com.petrovdns.vaadin.data.dto.UserDTO;
import com.petrovdns.vaadin.data.entity.Company;
import com.petrovdns.vaadin.data.entity.Roles;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

public class UserForm extends FormLayout {
    Binder<UserDTO> binder = new BeanValidationBinder<>(UserDTO.class);

    TextField username = new TextField("Username");
    TextField password = new TextField("Password");
    TextField newPassword = new TextField("New Password");
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    TextField email = new TextField("E-Mail");
    MultiSelectComboBox<Roles> roles = new MultiSelectComboBox<>("Roles");
    ComboBox<Company> company = new ComboBox<>("Company");

    private UserDTO userDTO;

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");


    public UserForm(List<Company> companies, List<Roles> role) {
        addClassName("userEntity-form");

        binder.bindInstanceFields(this);

        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);

        roles.setItems(role);
        roles.setItemLabelGenerator(Roles::getRoleName);

    }

    public void clear() {
        username.clear();
        password.clear();
        firstName.clear();
        lastName.clear();
        email.clear();
        roles.clear();
        company.clear();
    }

    public Component createButtonLayout() {
        Div buttonsGroup = new Div();
        buttonsGroup.addClassNames("btn-group");

        HorizontalLayout layout = new HorizontalLayout();

        save.addClickListener(event -> validateAndSave());
        save.addClassNames("btn btn-outline-primary");
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, userDTO)));
        delete.addClassNames("btn btn-outline-primary");
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));
        cancel.addClassNames("btn btn-outline-primary");

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        buttonsGroup.add(save, delete, cancel);
        layout.add(buttonsGroup);
        layout.getElement().getStyle().set("margin-top", "10px");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return layout;
    }

    public void setContact(UserDTO userDTO) {
        this.userDTO = userDTO;
        binder.readBean(userDTO);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(userDTO); //new UserDTO;
            if (!newPassword.getValue().isEmpty()) {
                userDTO.setPassword("{noop}" + newPassword.getValue());
                newPassword.clear();
            } else {
                if(!userDTO.getPassword().contains("{noop}")) {
                    userDTO.setPassword("{noop}" + password.getValue());
                }
            }
            fireEvent(new SaveEvent(this, userDTO));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private final UserDTO userDTO;

        protected UserFormEvent(UserForm source, UserDTO userDTO) {
            super(source, false);
            this.userDTO = userDTO;
        }

        public UserDTO getContact() {
            return userDTO;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        public SaveEvent(UserForm source, UserDTO userDTO) {
            super(source, userDTO);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        public DeleteEvent(UserForm source, UserDTO userDTO) {
            super(source, userDTO);
        }

    }

    public static class CloseEvent extends UserFormEvent {
        public CloseEvent(UserForm source) {
            super(source, null);

        }
    }

    public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        addListener(DeleteEvent.class, listener);
    }

    public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
        addListener(SaveEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<CloseEvent> listener) {
        addListener(CloseEvent.class, listener);
    }

}
