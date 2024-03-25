package br.com.vcruz.MoonProject.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import br.com.vcruz.MoonProject.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public List<User> findAll() {
    return userRepository.findAllDeletedFalse();
  }

  public User findById(Long id) {
    User user = userRepository.findByIdAndDeletedFalse(id)
        .orElseThrow(() -> {
          log.error("User {} not found.", id);
          throw new NotFoundException("user.notFound");
        });

    return user;
  }

  public User findByEmail(String email) {
    return userRepository.findByEmailAndDeletedFalse(email)
        .orElseThrow(() -> {
          log.error("User {} not found.", email);
          throw new NotFoundException("user.notFound");
        });
  }

  @Transactional(dontRollbackOn = { RuntimeException.class })
  public void countLoginAttempt(String email) {
    var user = this.findByEmail(email);
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

  public void resetLoginAttemp(String email) {
    var user = this.findByEmail(email);

    if (user.getBlockedUntil() != null && user.getBlockedUntil().isAfter(LocalDateTime.now())) {
      throw new AuthenticationException("user.blocked");
    }

    user.setLoginAttempts((byte) 0);
    user.setBlockedUntil(null);

    userRepository.save(user);
  }
}
