package ru.otus.ms.core.updates.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.otus.ms.core.updates.dtos.users.UserCreateRqDto;
import ru.otus.ms.core.updates.dtos.users.UsersDto;
import ru.otus.ms.core.updates.entities.Users;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface UsersMapper {

    UsersDto convertUsersToDto(Users users);

    Users convertNewUserToEntity(UserCreateRqDto userCreateRqDto);

    UserCreateRqDto convertNewUserToDto(Users users);


}
