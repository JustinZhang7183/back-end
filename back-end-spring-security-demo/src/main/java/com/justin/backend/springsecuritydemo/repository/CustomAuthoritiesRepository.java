package com.justin.backend.springsecuritydemo.repository;

import com.justin.backend.springsecuritydemo.model.CustomAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomAuthoritiesRepository extends JpaRepository<CustomAuthorities, Integer> {
}
