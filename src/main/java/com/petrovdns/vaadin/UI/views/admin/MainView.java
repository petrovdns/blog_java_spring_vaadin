package com.petrovdns.vaadin.UI.views.admin;

import com.petrovdns.vaadin.UI.views.navbar.MainLayout;
import com.petrovdns.vaadin.data.dto.UserDTO;
import com.petrovdns.vaadin.data.entity.UserEntity;
import com.petrovdns.vaadin.data.service.BlogService;
import com.petrovdns.vaadin.data.service.RolesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@PageTitle("Users | Petrov BLOG")
@Route(value = "user-list", layout = MainLayout.class)
@PermitAll
public class MainView extends VerticalLayout {

    Grid<UserDTO> grid = new Grid<>(UserDTO.class);
    TextField filterText = new TextField();
    UserForm form;

    BlogService blogService;
    RolesService rolesService;

    @Autowired
    public MainView(BlogService blogService, RolesService rolesService) {
        this.blogService = blogService;
        this.rolesService = rolesService;
        addClassNames("list-view");
        setSizeFull();

        configureGrid();
        configureContactForm();

        add(
                getToolBar(),
                getContent()
        );

        updateList();
        closeEditor();
    }

    private void updateList() {
        grid.setItems(blogService.findAllContacts(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureContactForm() {
        form = new UserForm(blogService.findAllCompanies(), rolesService.findAllRoles());
        form.setWidth("25em");

        //form.addSaveListener(save -> saveContact(save));
        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveContact(UserForm.SaveEvent event) {
        blogService.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(UserForm.DeleteEvent event) {
        blogService.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by First Name, Last Name and Email ...");
        filterText.setWidth("25em");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());


        Button addContactButton = new Button("Add User");
        addContactButton.addClickListener(e -> addContact());
        addContactButton.addClassNames("btn btn-outline-primary");

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.setWidthFull();
        toolbar.addClassNames("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        form.removeAll();
        form.add(form.username, form.password, form.firstName, form.lastName, form.email, form.roles, form.company, form.createButtonLayout());
        editContact(new UserDTO());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(UserDTO::getFormattedRoles)
                .setHeader("Roles");
        //grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> {
            form.removeAll();
            form.add(form.username, form.newPassword, form.firstName, form.lastName, form.email, form.roles, form.company, form.createButtonLayout());
            editContact(e.getValue());
        });
    }

    private void editContact(UserDTO userDTO) {
        if (userDTO == null) {
            closeEditor();
        } else {
            form.setContact(userDTO);
            form.setVisible(true);
            addClassName("editing");
        }

    }

    private void closeEditor() {
        form.setContact(null);
        form.clear();
        form.removeAll();
        form.setVisible(false);
        removeClassName("editing");
    }
}
