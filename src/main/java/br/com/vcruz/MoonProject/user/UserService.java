package br.com.vcruz.MoonProject.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.vcruz.MoonProject.exception.NotFoundException;
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

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmailAndDeletedFalse(email);
  }
}
