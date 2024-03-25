package br.com.vcruz.MoonProject.security;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.vcruz.MoonProject.exception.ExceptionResponseDto;
import br.com.vcruz.MoonProject.exception.ValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  @Autowired
  private JwtService jwtService;

  @Autowired
  private MoonUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    ExceptionResponseDto exceptionResponseDto = null;

    try {
      this.checkToken(request, response);
    } catch (ExpiredJwtException e) {
      exceptionResponseDto = new ExceptionResponseDto("jwt.expired", UNAUTHORIZED);
    } catch (UsernameNotFoundException | SignatureException e) {
      exceptionResponseDto = new ExceptionResponseDto("jwt.invalid", UNAUTHORIZED);
    } catch (ValidationException e) {
      exceptionResponseDto = new ExceptionResponseDto(e.getMessage(), UNAUTHORIZED);
    } finally {
      if (exceptionResponseDto != null) {
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        response.setStatus(exceptionResponseDto.getStatus().value());
        response.setContentType("application/json");
        writer.write(mapper.writeValueAsString(exceptionResponseDto));
        writer.flush();
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  private void checkToken(HttpServletRequest request, HttpServletResponse response) {
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String email = null;

    if (!request.getRequestURI().contains("/auth/login") && authHeader == null) {
      throw new ValidationException("token.notFound");
    }

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.split(" ")[1].trim();
      email = jwtService.extractEmail(token);
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(email);
      if (jwtService.validateToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
  }
}
