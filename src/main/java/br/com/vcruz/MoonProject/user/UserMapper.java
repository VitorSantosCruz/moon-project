package br.com.vcruz.MoonProject.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserResponseDto userToUserResponseDto(User user);

  User userRequestDtoToUser(UserRequestDto userRequestDto);

  List<UserResponseDto> usersToUsersResponseDto(List<User> users);

  List<User> usersRequestDtoToUsers(List<UserRequestDto> usersRequestDto);
}
