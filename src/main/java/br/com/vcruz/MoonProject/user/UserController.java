package br.com.vcruz.MoonProject.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  @PreAuthorize("hasRole('ROOT') or hasRole('ADMIN') or hasRole('LIST_USER')")
  public List<UserResponseDto> findAll() {
    var users = userService.findAll();
    var usersResponseDto = UserMapper.INSTANCE.usersToUsersResponseDto(users);

    return usersResponseDto;
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ROOT') or hasRole('ADMIN') or hasRole('FIND_USER')")
  public UserResponseDto findById(@PathVariable Long id) {
    var user = userService.findById(id)
        .orElseThrow(() -> {
          log.error("User {} not found.", id);
          throw new NotFoundException("user.notFound");
        });
    var userResponseDto = UserMapper.INSTANCE.userToUserResponseDto(user);

    return userResponseDto;
  }

  @PostMapping
  @PreAuthorize("hasRole('ROOT') or hasRole('ADMIN') or hasRole('REGISTER_USER')")
  public UserResponseDto save(@RequestBody UserRequestDto userRequestDto) {
    var userToBeSaved = UserMapper.INSTANCE.userRequestDtoToUser(userRequestDto);
    var savedUser = userService.save(userToBeSaved);
    var user = userService.findById(savedUser.getId()).get();
    var userResponseDto = UserMapper.INSTANCE.userToUserResponseDto(user);

    return userResponseDto;
  }
}
