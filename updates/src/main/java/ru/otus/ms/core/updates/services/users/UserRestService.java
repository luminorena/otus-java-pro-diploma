package ru.otus.ms.core.updates.services.users;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.ms.core.updates.dtos.users.UserCreateRqDto;
import ru.otus.ms.core.updates.dtos.users.UsersDto;
import ru.otus.ms.core.updates.entities.Users;

import java.util.Optional;
import java.util.UUID;


public interface UserRestService {

    Page<Users> getAllUsers(int page, int size, String sortBy);

    Optional<UsersDto> getUserById(UUID uuid);

    @Transactional
    UserCreateRqDto createNewUser(UserCreateRqDto userCreateRqDto);

    @Transactional
    void deleteUserById(UUID uuid);

    @Transactional
    boolean deleteUser(UUID uuid);

    @Transactional
    void updateUserById(@NonNull UUID uuid, UserCreateRqDto userCreateRqDto);

    @Transactional
    void deleteUserExtraData(UUID uuid);


}


