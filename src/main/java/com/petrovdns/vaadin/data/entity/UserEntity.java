package com.petrovdns.vaadin.data.entity;

import com.petrovdns.vaadin.data.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.bind.DefaultValue;


import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class UserEntity extends AbstractEntity {

    @NotEmpty(message = "The username field must not be empty")
    private String username;

    @NotEmpty(message = "The password field must not be empty")
    private String password;

    @NotEmpty(message = "The first name field must not be empty")
    private String firstName = "";

    @NotEmpty(message = "The last name field must not be empty")
    private String lastName = "";

    @ManyToOne
    @JoinColumn(name = "companies_id")
    private Company company;

    @OneToMany(mappedBy = "author")
    private List<BlogPost> blogPost;

    @ManyToOne
    @JoinColumn(name = "statuses_id")
    private Status status;

    @Email
    @NotEmpty(message = "The email field must not be empty")
    private String email = "";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<Roles> roles;

    @Override
    public String toString() {
        return firstName + " " + lastName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }
}