package br.com.vcruz.MoonProject.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.vcruz.MoonProject.exception.ValidationException;
import br.com.vcruz.MoonProject.role.RoleRequestDto;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
  @Autowired
  private UserService userService;

  @Autowired
  private Flyway flyway;

  private final String VALID_NAME = "user test";
  private final String VALID_EMAIL = "test@moon.com.br";
  private final String VALID_PASSWORD = "@Teste123";
  private final List<RoleRequestDto> VALID_ROLES = new ArrayList<RoleRequestDto>();
  private final String INVALID_NAME = "inva";
  private final String INVALID_EMAIL = "invalidEmail";
  private final String INVALID_PASSWORD = "invalidPassword";
  private final List<RoleRequestDto> INVALID_ROLES = new ArrayList<RoleRequestDto>();
  private final String EXISTING_EMAIL = "root@moon.com.br";

  {
    VALID_ROLES.add(new RoleRequestDto(3L));
    INVALID_ROLES.add(new RoleRequestDto(-1L));
  }

  @BeforeEach
  public void limparBancoDeDados() {
    flyway.clean();
    flyway.migrate();
  }

  @Test
  void shouldBeSaveUserWhenUserIsValid() {
    var userRequestDto = UserRequestDto
        .builder()
        .name(VALID_NAME)
        .email(VALID_EMAIL)
        .password(VALID_PASSWORD)
        .roles(VALID_ROLES)
        .build();
    var userToBeSaved = UserMapper.INSTANCE.userRequestDtoToUser(userRequestDto);

    assertDoesNotThrow(() -> userService.save(userToBeSaved));
  }

  @Test
  void shouldBeThrowErrorWhenUserIsInvalid() {
    var userRequestDto = UserRequestDto
        .builder()
        .name(INVALID_NAME)
        .email(INVALID_EMAIL)
        .password(INVALID_PASSWORD)
        .roles(INVALID_ROLES)
        .build();
    var userToBeSaved = UserMapper.INSTANCE.userRequestDtoToUser(userRequestDto);

    var exception = assertThrows(ValidationException.class, () -> userService.save(userToBeSaved));

    assertEquals("user.invalid", exception.getMessage());
    assertTrue(exception.getErrors().containsKey("name"));
    assertTrue(exception.getErrors().get("name").contains("name.invalid"));
    assertTrue(exception.getErrors().containsKey("email"));
    assertTrue(exception.getErrors().get("email").contains("email.invalid"));
    assertTrue(exception.getErrors().containsKey("password"));
    assertTrue(exception.getErrors().get("password").contains("password.invalid"));
    assertTrue(exception.getErrors().containsKey("roles"));
    assertTrue(exception.getErrors().get("roles").contains("roles.invalid"));
  }

  @Test
  void shouldBeThrowErrorWhenUserEmailAlreadyExists() {
    var userRequestDto = UserRequestDto
        .builder()
        .name(VALID_NAME)
        .email(EXISTING_EMAIL)
        .password(VALID_PASSWORD)
        .roles(VALID_ROLES)
        .build();
    var userToBeSaved = UserMapper.INSTANCE.userRequestDtoToUser(userRequestDto);

    var exception = assertThrows(ValidationException.class, () -> userService.save(userToBeSaved));

    assertEquals("user.invalid", exception.getMessage());
    assertTrue(exception.getErrors().containsKey("email"));
    assertTrue(exception.getErrors().get("email").contains("email.alreadyExists"));
  }
}
