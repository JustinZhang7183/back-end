package com.justin.backend.authorizationserver.repository;

import com.justin.backend.authorizationserver.entities.CustomAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomAuthoritiesRepository extends JpaRepository<CustomAuthorities, Integer> {
}
