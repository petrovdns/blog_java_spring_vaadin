package com.petrovdns.vaadin.data.repository;

import com.petrovdns.vaadin.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

public interface ContactRepository extends JpaRepository<UserEntity, Long> {

//    @Query("select c from UserEntity c " +
//            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
//            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")

    @Query("select c from UserEntity c " +
            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(c.email) like lower(concat('%', :searchTerm, '%'))")
    List<UserEntity> search(@Param("searchTerm") String searchTerm);

    UserEntity findByUsername(String username);
}
