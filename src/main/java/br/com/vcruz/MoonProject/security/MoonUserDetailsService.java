package br.com.vcruz.MoonProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.vcruz.MoonProject.exception.NotFoundException;
import br.com.vcruz.MoonProject.user.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MoonUserDetailsService implements UserDetailsService {
  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    try {
      var user = userService.findByEmail(email)
          .orElseThrow(() -> {
            log.error("User {} not found.", email);
            throw new NotFoundException("user.notFound");
          });
      return new MoonUserDetails(user);
    } catch (Exception e) {
      throw new UsernameNotFoundException(e.getMessage());
    }
  }
}
