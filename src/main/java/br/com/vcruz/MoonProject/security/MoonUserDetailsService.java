package br.com.vcruz.MoonProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import br.com.vcruz.MoonProject.user.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MoonUserDetailsService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var user = userRepository.findByEmailAndDeletedFalse(email)
        .orElseThrow(() -> {
          log.error("User {} not found.", email);
          throw new UsernameNotFoundException("user.notFound");
        });

    if (user.isConfirmed()) {
      return new MoonUserDetails(user);
    } else {
      log.error("User {} found but not confirmed.", email);
      throw new AuthenticationException("user.notConfirmed");
    }
  }
}
