package br.com.vcruz.MoonProject.user;

import java.util.ArrayList;
import java.util.List;

import br.com.vcruz.MoonProject.role.Role;
import br.com.vcruz.MoonProject.role.RoleResponseDto;

public class UserMapperImpl implements UserMapper {
  @Override
  public UserResponseDto userToUserResponseDto(User user) {
    if (user == null) {
      return null;
    }

    List<RoleResponseDto> roles = new ArrayList<>();

    for (var role : user.getRoles()) {
      roles.add(new RoleResponseDto(role.getId(), role.getName()));
    }

    return new UserResponseDto(
        user.getId(),
        user.getName(),
        user.getEmail(),
        roles);
  }

  @Override
  public User userRequestDtoToUser(UserRequestDto userRequestDto) {
    if (userRequestDto == null) {
      return null;
    }

    List<Role> roles = new ArrayList<>();

    for (var role : userRequestDto.roles()) {
      roles.add(new Role(role.id(), null, null));
    }

    return User.builder()
        .name(userRequestDto.name())
        .password(userRequestDto.password())
        .email(userRequestDto.email())
        .roles(roles)
        .build();
  }

  @Override
  public List<UserResponseDto> usersToUsersResponseDto(List<User> users) {
    if (users == null) {
      return null;
    }

    List<UserResponseDto> usersResponseDto = new ArrayList<>(users.size());

    for (var user : users) {
      usersResponseDto.add(
          this.userToUserResponseDto(user));
    }

    return usersResponseDto;
  }

  @Override
  public List<User> usersRequestDtoToUsers(List<UserRequestDto> usersRequestDto) {
    if (usersRequestDto == null) {
      return null;
    }

    List<User> users = new ArrayList<>(usersRequestDto.size());

    for (var userRequestDto : usersRequestDto) {
      users.add(
          this.userRequestDtoToUser(userRequestDto));
    }

    return users;
  }
}
