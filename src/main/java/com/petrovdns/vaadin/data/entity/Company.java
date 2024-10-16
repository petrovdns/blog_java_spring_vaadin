package com.petrovdns.vaadin.data.entity;

import com.petrovdns.vaadin.data.AbstractEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Formula;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="companies")
public class Company extends AbstractEntity {
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "company")
    @Nullable
    private List<UserEntity> employees = new LinkedList<>();

    @Formula("(select count(c.id) from users c where c.companies_id = id)")
    private int employeeCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(List<UserEntity> employees) {
        this.employees = employees;
    }

    public int getEmployeeCount(){
        return employeeCount;
    }
}