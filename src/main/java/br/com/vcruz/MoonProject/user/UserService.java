package br.com.vcruz.MoonProject.user;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import br.com.vcruz.MoonProject.exception.ValidationException;
import br.com.vcruz.MoonProject.role.Role;
import br.com.vcruz.MoonProject.role.RoleService;
import jakarta.transaction.Transactional;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleService roleService;

  public List<User> findAll() {
    return userRepository.findAllDeletedFalse();
  }

  public Optional<User> findById(Long id) {
    return userRepository.findByIdAndDeletedFalse(id);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmailAndDeletedFalse(email);
  }

  @Transactional
  public User save(User user) {
    this.isValid(user);
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return this.userRepository.save(user);
  }

  private void isValid(User user) {
    Map<String, String> errors = new HashMap<>();

    if (!this.nameIsValid(user.getName())) {
      errors.put("name", "name.invalid");
    }

    if (!this.passwordIsValid(user.getPassword())) {
      errors.put("password", "password.invalid");
    }

    try {
      if (!this.emailIsValid(user.getEmail())) {
        errors.put("email", "email.invalid");
      }
    } catch (ValidationException e) {
      errors.put("email", e.getMessage());
    }

    if (!this.rolesIsValid(user.getRoles())) {
      errors.put("roles", "roles.invalid");
    }

    if (errors.size() > 0) {
      throw new ValidationException("user.invalid", errors);
    }
  }

  private boolean nameIsValid(String name) {
    return !name.trim().isEmpty() &&
        name.trim().length() >= 5;
  }

  private boolean emailIsValid(String email) {
    this.findByEmail(email)
        .ifPresent((user) -> {
          throw new ValidationException("email.alreadyExists");
        });
    String regexPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    Matcher matcher = Pattern.compile(regexPattern).matcher(email);
    return matcher.matches();
  }

  private boolean passwordIsValid(String password) {
    String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";
    return Pattern.compile(regexPattern).matcher(password).matches();

  }

  private boolean rolesIsValid(List<Role> roles) {
    if (roles != null && roles.size() > 0) {
      for (var role : roles) {
        if (roleService.findById(role.getId()).isEmpty()) {
          return false;
        }
      }

      return true;
    }

    return false;
  }

  @Transactional(dontRollbackOn = { RuntimeException.class })
  public void countLoginAttempt(String email) {
    this.findByEmail(email)
        .ifPresent((user) -> {
          if (user.isConfirmed()) {
            var loginAttemps = user.getLoginAttempts();

            if (loginAttemps < 5) {
              user.setLoginAttempts(++loginAttemps);
            }

            if (loginAttemps == 5) {
              if (user.getBlockedUntil() != null) {
                user.setBlockedUntil(user.getBlockedUntil().plusMinutes(5));
              } else {
                user.setBlockedUntil(LocalDateTime.now().plusMinutes(5));
              }
            }

            userRepository.save(user);
          }
        });
  }

  public void resetLoginAttemp(String email) {
    this.findByEmail(email)
        .ifPresent((user) -> {
          if (user.getBlockedUntil() != null && user.getBlockedUntil().isAfter(LocalDateTime.now())) {
            throw new AuthenticationException("user.blocked");
          }

          user.setLoginAttempts((byte) 0);
          user.setBlockedUntil(null);

          userRepository.save(user);
        });
  }
}
