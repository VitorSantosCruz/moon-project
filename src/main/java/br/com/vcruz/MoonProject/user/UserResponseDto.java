package br.com.vcruz.MoonProject.user;

import java.util.Set;

import br.com.vcruz.MoonProject.role.Role;

public record UserResponseDto(Long id, String name, String email, Set<Role> roles) {
}
