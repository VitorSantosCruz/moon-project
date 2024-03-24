package br.com.vcruz.MoonProject.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.vcruz.MoonProject.user.User;
import br.com.vcruz.MoonProject.user.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> optionalUser = userService.findByEmail(email);

    var user = optionalUser.orElseThrow(() -> {
      log.error("E-mail {} not found", email);
      throw new UsernameNotFoundException("user.notFound");
    });

    return new CustomUserDetails(user);
  }
}
