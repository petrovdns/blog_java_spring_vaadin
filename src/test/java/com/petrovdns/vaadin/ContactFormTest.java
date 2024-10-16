//package com.petrovdns.vaadin;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicReference;
//
//import com.petrovdns.vaadin.UI.views.admin.UserForm;
//import com.petrovdns.vaadin.data.entity.UserEntity;
//import com.petrovdns.vaadin.data.entity.Status;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import com.petrovdns.vaadin.data.entity.Company;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.util.Assert;
//
//public class ContactFormTest {
//    private List<Company> companies;
//    private List<Status> statuses;
//    private UserEntity marcUsher;
//    private Company company1;
//    private Company company2;
//    private Status status1;
//    private Status status2;
//
//    @BeforeEach
//    public void setupData() {
//        companies = new ArrayList<>();
//        company1 = new Company();
//        company1.setName("Vaadin Ltd");
//        company2 = new Company();
//        company2.setName("IT Mill");
//        companies.add(company1);
//        companies.add(company2);
//
//        statuses = new ArrayList<>();
//        status1 = new Status();
//        status1.setName("Status 1");
//        status2 = new Status();
//        status2.setName("Status 2");
//        statuses.add(status1);
//        statuses.add(status2);
//
//        marcUsher = new UserEntity();
//        marcUsher.setFirstName("Marc");
//        marcUsher.setLastName("Usher");
//        marcUsher.setEmail("marc@usher.com");
//        marcUsher.setStatus(status1);
//        marcUsher.setCompany(company2);
//    }
//
//    @Test
//    public void formFieldsPopulated() {
//        UserForm form = new UserForm(companies, statuses);
//        form.setContact(marcUsher);
//
//        Assertions.assertEquals("Marc", form.firstName.getValue());
//        Assertions.assertEquals("Usher", form.lastName.getValue());
//        Assertions.assertEquals("marc@usher.com", form.email.getValue());
//        Assertions.assertEquals(company2, form.company.getValue());
//        Assertions.assertEquals(status1, form.status.getValue());
//
//    }
//
//    @Test
//    public void saveEventHasCorectValue() {
//        UserForm form = new UserForm(companies, statuses);
//        UserEntity contact = new UserEntity();
//        form.setContact(contact);
//
//        form.firstName.setValue("John");
//        form.lastName.setValue("Doe");
//        form.email.setValue("john@doe.com");
//        form.company.setValue(company1);
//        form.status.setValue(status2);
//
//        AtomicReference<UserEntity> savedContact = new AtomicReference<>(null);
//        form.addSaveListener(e -> savedContact.set(e.getContact()));
//
//        form.save.click();
//
//        UserEntity saved = savedContact.get();
//
//        Assertions.assertEquals("John", saved.getFirstName());
//        Assertions.assertEquals("Doe", saved.getLastName());
//        Assertions.assertEquals("john@doe.com", saved.getEmail());
//        Assertions.assertEquals(company1, saved.getCompany());
//        Assertions.assertEquals(status2, saved.getStatus());
//
//
//    }
//
//
//
//}
