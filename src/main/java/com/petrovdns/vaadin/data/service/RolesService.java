package com.petrovdns.vaadin.data.service;

import com.petrovdns.vaadin.data.entity.Roles;
import com.petrovdns.vaadin.data.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@Service
public class RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    public List<Roles> findAllRoles() {
       return rolesRepository.findAll();
    }
}
