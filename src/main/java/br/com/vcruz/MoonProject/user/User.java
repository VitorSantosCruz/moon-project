package br.com.vcruz.MoonProject.user;

import java.time.LocalDateTime;
import java.util.List;

import br.com.vcruz.MoonProject.action.Action;
import br.com.vcruz.MoonProject.entity.AbstractAuditingEntity;
import br.com.vcruz.MoonProject.role.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractAuditingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String password;

  private String email;

  private boolean deleted;

  private boolean confirmed;

  private byte loginAttempts;

  private LocalDateTime blockedUntil;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Action> actions;
}
