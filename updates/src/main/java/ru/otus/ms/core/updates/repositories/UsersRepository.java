package ru.otus.ms.core.updates.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.otus.ms.core.updates.entities.Users;

import java.util.UUID;


@Repository
public interface UsersRepository extends PagingAndSortingRepository<Users, UUID> {
    Users findById(@NonNull UUID id);

    Users save(Users users);

    @Modifying
    @Query("DELETE FROM Users n WHERE n.id = :uuid")
    void deleteUserById(@NonNull @Param("uuid") UUID uuid);




}
