package com.justin.backend.authorizationserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;

@Data
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

  private String firstName;

  private String lastName;

  @Transient
  private String fullName;

  private String emailAddress;

  private LocalDate birthDate;

//  @Singular
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinTable(
      name = "custom_users_authorities",
      foreignKey = @ForeignKey(NO_CONSTRAINT),
      inverseForeignKey = @ForeignKey(NO_CONSTRAINT),
      joinColumns = {@JoinColumn(name = "custom_users_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "custom_authorities_id", referencedColumnName = "id")}
  )
  private Set<CustomAuthorities> customAuthorities;

  private Boolean accountNonExpired = true;

  private Boolean accountNonLocked = true;

  private Boolean credentialsNonExpired = true;

  private Boolean enabled = true;
}
