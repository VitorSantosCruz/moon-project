package br.com.vcruz.MoonProject.user;

import java.util.List;

import br.com.vcruz.MoonProject.role.RoleRequestDto;
import lombok.Builder;

@Builder
public record UserRequestDto(
    String name,
    String password,
    String email,
    List<RoleRequestDto> roles) {
}
