package com.petrovdns.vaadin.data.repository;

import com.petrovdns.vaadin.data.entity.Roles;
import com.petrovdns.vaadin.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

public interface RolesRepository extends JpaRepository<Roles, Long> {
}
