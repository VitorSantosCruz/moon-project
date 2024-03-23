package br.com.vcruz.MoonProject.user;

import java.util.Set;

import br.com.vcruz.MoonProject.role.Role;

public record UserRequestDto(
    String name,
    String password,
    String email,
    Set<Role> roles) {
}
