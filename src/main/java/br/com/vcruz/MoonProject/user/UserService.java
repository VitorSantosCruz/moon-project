package br.com.vcruz.MoonProject.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import jakarta.transaction.Transactional;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public List<User> findAll() {
    return userRepository.findAllDeletedFalse();
  }

  public Optional<User> findById(Long id) {
    return userRepository.findByIdAndDeletedFalse(id);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmailAndDeletedFalse(email);
  }

  @Transactional(dontRollbackOn = { RuntimeException.class })
  public void countLoginAttempt(String email) {
    this.findByEmail(email)
        .ifPresent((user) -> {
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
