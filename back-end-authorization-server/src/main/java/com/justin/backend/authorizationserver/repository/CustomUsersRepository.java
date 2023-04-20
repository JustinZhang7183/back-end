package com.justin.backend.authorizationserver.repository;

import com.justin.backend.authorizationserver.entities.CustomUsers;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomUsersRepository extends JpaRepository<CustomUsers, Integer> {
  // if no this annotation, it will query twice to set authorities by the annotation in authorities filed in CustomUsers
  @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
      attributePaths = {"customAuthorities"})
  Optional<CustomUsers> findByUsername(String username);
}
