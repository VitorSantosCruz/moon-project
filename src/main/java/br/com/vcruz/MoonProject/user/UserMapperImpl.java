package br.com.vcruz.MoonProject.user;

import java.util.ArrayList;
import java.util.List;

public class UserMapperImpl implements UserMapper {
  @Override
  public UserResponseDto userToUserResponseDto(User user) {
    if (user == null) {
      return null;
    }

    return new UserResponseDto(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getRoles());
  }

  @Override
  public User userRequestDtoToUser(UserRequestDto userRequestDto) {
    if (userRequestDto == null) {
      return null;
    }

    return User.builder()
        .name(userRequestDto.name())
        .password(userRequestDto.password())
        .email(userRequestDto.email())
        .roles(userRequestDto.roles())
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
