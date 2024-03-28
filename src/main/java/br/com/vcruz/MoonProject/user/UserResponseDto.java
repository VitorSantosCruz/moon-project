package br.com.vcruz.MoonProject.user;

import java.util.List;

import br.com.vcruz.MoonProject.role.RoleResponseDto;

public record UserResponseDto(Long id, String name, String email, List<RoleResponseDto> roles) {
}
