package ru.otus.ms.core.updates.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.otus.ms.core.updates.entities.UpdateHistory;
import ru.otus.ms.core.updates.entities.Updates;

import java.util.UUID;

public interface UpdatesRepository extends PagingAndSortingRepository<Updates, UUID> {
    Updates findById(@NonNull UUID id);

    Updates save(Updates updates);

    @Modifying
    @Query("DELETE FROM Updates n WHERE n.id = :uuid")
    void deleteUpdateById(@NonNull @Param("uuid") UUID uuid);

    @Query("SELECT uh FROM UpdateHistory uh WHERE uh.originalUpdateId = :uuid")
    UpdateHistory findDeletedUpdate(@NonNull @Param("uuid") UUID uuid);

}
