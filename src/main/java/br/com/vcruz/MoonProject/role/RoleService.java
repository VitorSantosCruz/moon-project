package br.com.vcruz.MoonProject.role;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
  @Autowired
  private RoleRepository roleRepository;

  public Optional<Role> findById(long id) {
    return roleRepository.findById(id);
  }
}
