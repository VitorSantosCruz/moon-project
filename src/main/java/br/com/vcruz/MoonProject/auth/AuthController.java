package br.com.vcruz.MoonProject.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import br.com.vcruz.MoonProject.security.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequestDTO.email(), authRequestDTO.password()));

      if (authentication.isAuthenticated()) {
        return new JwtResponseDTO(jwtService.GenerateToken(authRequestDTO.email()));
      } else {
        throw new AuthenticationException("auth.invalid");
      }
    } catch (BadCredentialsException e) {
      throw new AuthenticationException("auth.invalid");
    }
  }
}
