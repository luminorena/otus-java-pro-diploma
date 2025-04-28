package ru.otus.ms.core.updates.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.otus.ms.core.updates.entities.UserUpdates;

import java.util.UUID;

@Repository
public interface UserUpdatesRepository extends JpaRepository<UserUpdates, UUID> {

    @Modifying
    @Query("DELETE FROM UserUpdates n WHERE n.userId = :uuid")
    void deleteUserFromUserUpdates(@NonNull @Param("uuid") UUID uuid);

    @Modifying
    @Query("DELETE FROM UserUpdates n WHERE n.updateId = :uuid")
    void deleteUpdateFromUserUpdates(@NonNull @Param("uuid") UUID uuid);


}
