package com.justin.backend.springsecuritydemo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "custom_users")
public class CustomUsers {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NonNull
  @Column(unique = true)
  private String username;

  @NonNull
  private String password;

  @Singular
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinTable(name = "custom_users_authorities", joinColumns = {
      @JoinColumn(name = "users_id", referencedColumnName = "id")},inverseJoinColumns = {
          @JoinColumn(name = "authorities_id", referencedColumnName = "id")})
  private Set<CustomAuthorities> authorities;

  @Builder.Default
  private Boolean accountNonExpired = true;

  @Builder.Default
  private Boolean accountNonLocked = true;

  @Builder.Default
  private Boolean credentialsNonExpired = true;

  @Builder.Default
  private Boolean enabled = true;
}
