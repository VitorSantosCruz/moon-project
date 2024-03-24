package br.com.vcruz.MoonProject.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import br.com.vcruz.MoonProject.security.JwtService;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {
  @Autowired
  AuthService authService;

  @Autowired
  JwtService jwtService;

  @Autowired
  private Flyway flyway;

  private final String VALID_EMAIL = "root@moon.com.br";
  private final String VALID_PASSWORD = "root";
  private final String INVALID_EMAIL = "invalid@moon.com.br";;
  private final String INVALID_PASSWORD = "invalid";

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
    assertThrows(AuthenticationException.class, () -> {
      AuthRequestDTO authDto = new AuthRequestDTO(VALID_EMAIL, INVALID_PASSWORD);
      this.authService.login(authDto);
    });
  }

  @Test
  void shouldBeThrowErrorWhenEmailIsInvalid() {
    assertThrows(AuthenticationException.class, () -> {
      AuthRequestDTO authDto = new AuthRequestDTO(INVALID_EMAIL, VALID_PASSWORD);
      this.authService.login(authDto);
    });
  }

  @Test
  void shouldBeThrowErrorWhenEmailAndPasswordAreInvalid() {
    assertThrows(AuthenticationException.class, () -> {
      AuthRequestDTO authDto = new AuthRequestDTO(INVALID_EMAIL, INVALID_PASSWORD);
      this.authService.login(authDto);
    });
  }
}
