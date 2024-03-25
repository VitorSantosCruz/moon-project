package br.com.vcruz.MoonProject.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vcruz.MoonProject.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping
  public List<UserResponseDto> findAll() {
    var users = userService.findAll();
    var usersResponseDto = UserMapper.INSTANCE.usersToUsersResponseDto(users);

    return usersResponseDto;
  }

  @GetMapping("/{id}")
  @ExceptionHandler({ NoSuchElementException.class })
  public UserResponseDto findById(@PathVariable Long id) {
    var user = userService.findById(id)
        .orElseThrow(() -> {
          log.error("User {} not found.", id);
          throw new NotFoundException("user.notFound");
        });
    var userResponseDto = UserMapper.INSTANCE.userToUserResponseDto(user);

    return userResponseDto;
  }
}
