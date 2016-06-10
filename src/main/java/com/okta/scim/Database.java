package com.okta.scim;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface Database extends JpaRepository<User, Long> {
    List<User> findById(String id);

    // Query by username
    @Query("SELECT u FROM User u WHERE u.userName = :name")
    Page<User> findByUsername(@Param("name") String name, Pageable pagable);

    // Query by active
    @Query("SELECT u FROM User u WHERE u.active = :value")
    Page<User> findByActive(@Param("value") Boolean value, Pageable pagable);

    // Query by familyName
    @Query("SELECT u FROM User u WHERE u.familyName = :name")
    Page<User> findByFamilyName(@Param("name") String name, Pageable pagable);

    // Query by givenName
    @Query("SELECT u FROM User u WHERE u.givenName = :name")
    Page<User> findByGivenName(@Param("name") String name, Pageable pagable);

    // Query by tenant
    @Query("SELECT u FROM User u WHERE u.tenant = :name")
    Page<User> findByTenant(@Param("name") String name, Pageable pagable);

}

@EnableJpaRepositories
class Config {}
