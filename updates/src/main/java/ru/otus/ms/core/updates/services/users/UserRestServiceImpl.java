package ru.otus.ms.core.updates.services.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.ms.core.updates.dtos.users.UserCreateRqDto;
import ru.otus.ms.core.updates.dtos.users.UsersDto;
import ru.otus.ms.core.updates.entities.Users;
import ru.otus.ms.core.updates.exception_handlers.exceptions.NoResourceFoundException;
import ru.otus.ms.core.updates.mappers.UsersMapper;
import ru.otus.ms.core.updates.repositories.UserUpdatesRepository;
import ru.otus.ms.core.updates.repositories.UsersRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRestServiceImpl implements UserRestService {
    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;
    private final UserUpdatesRepository userUpdatesRepository;

    @Override
    public Page<Users> getAllUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return usersRepository.findAll(pageable);
    }


    @Override
    public Optional<UsersDto> getUserById(UUID uuid) {
        Users user = usersRepository.findById(uuid);
        return user != null ? Optional.of(usersMapper.convertUsersToDto(user)) : Optional.empty();
    }

    @Override
    public UserCreateRqDto createNewUser(UserCreateRqDto userCreateRqDto) {
        Users user = usersMapper.convertNewUserToEntity(userCreateRqDto);
        Users save = usersRepository.save(user);
        return usersMapper.convertNewUserToDto(save);
    }


    @Override
    public void deleteUserById(UUID uuid) {
        usersRepository.deleteUserById(uuid);
    }

    @Override
    public boolean deleteUser(UUID uuid) {
        return getUserById(uuid)
                .map(user -> {
                    deleteUserExtraData(uuid);
                    deleteUserById(uuid);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void deleteUserExtraData(UUID uuid){
        userUpdatesRepository.deleteUserFromUserUpdates(uuid);
    }


    @Override
    public void updateUserById(@NonNull UUID uuid, UserCreateRqDto userCreateRqDto) {
        Users user = usersRepository.findById(uuid);
        if (user != null) {
            user.setUserFio(userCreateRqDto.getUserFio());
            user.setUserEmail(userCreateRqDto.getUserEmail());
            usersRepository.save(user);
        } else throw new NoResourceFoundException("User with UUID " + uuid + " not found");
    }
}
