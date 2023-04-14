package com.justin.backend.springsecuritydemo.repository;

import com.justin.backend.springsecuritydemo.model.CustomUsers;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomUsersRepository extends JpaRepository<CustomUsers, Integer> {
  @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
      attributePaths = {"authorities"})
  Optional<CustomUsers> findByUsername(String username);

  @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
      attributePaths = {"authorities"})
  List<CustomUsers> findAll();
}
