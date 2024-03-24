package br.com.vcruz.MoonProject.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import br.com.vcruz.MoonProject.security.JwtService;

@Service
public class AuthService {
  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  public JwtResponseDTO login(AuthRequestDTO authRequestDTO) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequestDTO.email(), authRequestDTO.password()));

      return new JwtResponseDTO(jwtService.GenerateToken(authRequestDTO.email()));
    } catch (BadCredentialsException e) {
      throw new AuthenticationException("auth.invalid");
    }
  }
}
