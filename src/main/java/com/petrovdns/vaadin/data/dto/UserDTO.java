package com.petrovdns.vaadin.data.dto;

import com.petrovdns.vaadin.data.AbstractEntity;
import com.petrovdns.vaadin.data.entity.BlogPost;
import com.petrovdns.vaadin.data.entity.Company;
import com.petrovdns.vaadin.data.entity.Roles;
import com.petrovdns.vaadin.data.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

public class UserDTO extends AbstractEntity {

//    @NotEmpty
//    private String nickname;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty(message = "The first name field must not be empty")
    private String firstName = "";

    @NotEmpty(message = "The last name field must not be empty")
    private String lastName = "";

    private List<BlogPost> blogPost;

    @Email
    @NotEmpty(message = "The email field must not be empty")
    private String email = "";

    @NotNull(message = "The company field must not be empty")
    private Company company;

   // @NotNull(message = "The status field must not be empty")
    private Status status;

    @NotEmpty(message = "The status field must not be empty")
    private Set<Roles> roles;


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public String getFormattedRoles() {
       return roles.stream().map(Roles::getRoleName).collect(Collectors.joining(", "));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
