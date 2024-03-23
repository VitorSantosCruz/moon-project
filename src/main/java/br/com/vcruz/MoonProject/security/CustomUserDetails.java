package br.com.vcruz.MoonProject.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.vcruz.MoonProject.role.Role;
import br.com.vcruz.MoonProject.user.User;

public class CustomUserDetails extends User implements UserDetails {
  private String email;
  private String password;
  Collection<? extends GrantedAuthority> authorities;

  public CustomUserDetails(User user) {
    this.email = user.getEmail();
    this.password = user.getPassword();
    List<GrantedAuthority> auths = new ArrayList<>();

    for (Role role : user.getRoles()) {
      auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
    }

    this.authorities = auths;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
