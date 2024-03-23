package br.com.vcruz.MoonProject.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  @Query("FROM users u WHERE u.deleted = FALSE")
  List<User> findAllDeletedFalse();

  Optional<User> findByIdAndDeletedFalse(Long id);

  Optional<User> findByEmailAndDeletedFalse(String email);
}
