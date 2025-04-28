package ru.otus.ms.core.updates.services.updates;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.ms.core.updates.dtos.updates.UpdatedHistoryDto;
import ru.otus.ms.core.updates.dtos.updates.UpdatesCreateRqDto;
import ru.otus.ms.core.updates.dtos.updates.UpdatesDto;
import ru.otus.ms.core.updates.entities.Updates;

import java.util.Optional;
import java.util.UUID;


public interface UpdatesRestService {

    Page<Updates> getAllUpdates(int page, int size, String sortBy);

    Optional<UpdatesDto> getUpdateById(UUID uuid);

    @Transactional
    UpdatesCreateRqDto createNewUpdate(UpdatesCreateRqDto updatesCreateRqDto);

    @Transactional
    void deleteUpdateById(UUID uuid);

    @Transactional
    boolean deleteUpdate(UUID uuid);

    @Transactional
    void renewUpdate(@NonNull UUID uuid, UpdatesCreateRqDto updatesCreateRqDto);

    Optional<UpdatedHistoryDto> findDeletedUpdate(UUID uuid);

    void deleteUpdateExtraData(UUID uuid);
}
