package br.com.vcruz.MoonProject.user;

import java.util.List;

import br.com.vcruz.MoonProject.role.RoleRequestDto;

public record UserRequestDto(
    String name,
    String password,
    String email,
    List<RoleRequestDto> roles) {
}
