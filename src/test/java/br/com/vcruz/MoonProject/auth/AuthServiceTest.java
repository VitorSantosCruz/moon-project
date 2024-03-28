package br.com.vcruz.MoonProject.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.Optional;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.context.ActiveProfiles;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import br.com.vcruz.MoonProject.security.JwtService;
import br.com.vcruz.MoonProject.user.User;
import br.com.vcruz.MoonProject.user.UserService;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {
  @Autowired
  private AuthService authService;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private Flyway flyway;

  private final String VALID_EMAIL = "root@moon.com.br";
  private final String VALID_PASSWORD = "root";
  private final String INVALID_EMAIL = "invalid@moon.com.br";;
  private final String INVALID_PASSWORD = "invalid";
  private final String NOT_CONFIRMED_EMAIL = "not-confirmed@moon.com.br";
  private final String NOT_CONFIRMED_PASSWORD = "not-confirmed";

  private void doInvalidLoginWithTryCatch(AuthRequestDTO invalidAuthDTO) {
    try {
      this.authService.login(invalidAuthDTO);
    } catch (Exception e) {
    }
  }

  @BeforeEach
  public void limparBancoDeDados() {
    flyway.clean();
    flyway.migrate();
  }

  @Test
  void shouldBeReturnTokenWhenUserIsValid() {
    var authDto = new AuthRequestDTO(VALID_EMAIL, VALID_PASSWORD);
    var responseDTO = this.authService.login(authDto);
    var email = this.jwtService.extractEmail(responseDTO.accessToken());

    assertEquals(VALID_EMAIL, email);
  }

  @Test
  void shouldBeThrowErrorWhenPassordIsInvalid() {
    var authDto = new AuthRequestDTO(VALID_EMAIL, INVALID_PASSWORD);
    var exception = assertThrows(AuthenticationException.class, () -> {
      this.authService.login(authDto);
    });

    assertEquals("auth.invalid", exception.getMessage());
  }

  @Test
  void shouldBeThrowErrorWhenEmailIsInvalid() {
    var authDto = new AuthRequestDTO(INVALID_EMAIL, VALID_PASSWORD);
    var exception = assertThrows(AuthenticationException.class, () -> {
      this.authService.login(authDto);
    });

    assertEquals("auth.invalid", exception.getMessage());
  }

  @Test
  void shouldBeThrowErrorWhenEmailAndPasswordAreInvalid() {
    var authDto = new AuthRequestDTO(INVALID_EMAIL, INVALID_PASSWORD);
    var exception = assertThrows(AuthenticationException.class, () -> {
      this.authService.login(authDto);
    });

    assertEquals("auth.invalid", exception.getMessage());
  }

  @Test
  void shouldBeBlockUserLoginWhenLoginFailsFiveTimes() {
    var invalidAuthDto = new AuthRequestDTO(VALID_EMAIL, INVALID_PASSWORD);
    var validAuthDto = new AuthRequestDTO(VALID_EMAIL, VALID_PASSWORD);
    Optional<User> optionalUser;

    this.doInvalidLoginWithTryCatch(invalidAuthDto);
    this.doInvalidLoginWithTryCatch(invalidAuthDto);
    this.doInvalidLoginWithTryCatch(invalidAuthDto);
    this.doInvalidLoginWithTryCatch(invalidAuthDto);
    this.doInvalidLoginWithTryCatch(invalidAuthDto);

    var exception = assertThrows(AuthenticationException.class, () -> {
      this.authService.login(validAuthDto);
    });
    assertEquals("user.blocked", exception.getMessage());

    optionalUser = userService.findByEmail(VALID_EMAIL);

    if (optionalUser.isPresent()) {
      var user = optionalUser.get();
      assertEquals(user.getLoginAttempts(), 5);
      assertNotNull(user.getBlockedUntil());
      assertTrue(user.getBlockedUntil().isAfter(LocalDateTime.now()));
    } else {
      fail("User not be found.");
    }
  }

  @Test
  void shouldBeUnlockUserLoginWhenLoginIsValidAndBlockedUntilParameterIsBeforeNow() {
    var authDto = new AuthRequestDTO(VALID_EMAIL, VALID_PASSWORD);
    var optionalUser = userService.findByEmail(VALID_EMAIL);

    if (optionalUser.isPresent()) {
      var user = optionalUser.get();
      user.setLoginAttempts((byte) 5);
      user.setBlockedUntil(LocalDateTime.now().minusMinutes(5));
      var responseDTO = this.authService.login(authDto);
      var email = this.jwtService.extractEmail(responseDTO.accessToken());

      assertEquals(VALID_EMAIL, email);
    } else {
      fail("User not be found.");
    }
  }

  @Test
  void shouldBeThrowErrorWhenUserEmailNotConfirmed() {
    var authDto = new AuthRequestDTO(NOT_CONFIRMED_EMAIL, NOT_CONFIRMED_PASSWORD);
    var exception = assertThrows(InternalAuthenticationServiceException.class, () -> {
      this.authService.login(authDto);
    });

    assertEquals("user.notConfirmed", exception.getMessage());
  }
}
