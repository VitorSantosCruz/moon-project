package br.com.vcruz.MoonProject.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import br.com.vcruz.MoonProject.security.JwtService;
import br.com.vcruz.MoonProject.user.UserService;

@Service
public class AuthService {
  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  public JwtResponseDTO login(AuthRequestDTO authRequestDTO) {
    var email = authRequestDTO.email();

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(email, authRequestDTO.password()));

      userService.resetLoginAttemp(email);

      return new JwtResponseDTO(jwtService.GenerateToken(email));
    } catch (AuthenticationException | BadCredentialsException e) {
      userService.countLoginAttempt(email);
      var message = "auth.invalid";

      if (e instanceof AuthenticationException) {
        message = e.getMessage();
      }

      throw new AuthenticationException(message);
    }
  }
}
