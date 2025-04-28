package ru.otus.ms.core.updates.mappers;

import org.mapstruct.Mapper;
import ru.otus.ms.core.updates.dtos.updates.UpdatedHistoryDto;
import ru.otus.ms.core.updates.dtos.updates.UpdatesCreateRqDto;
import ru.otus.ms.core.updates.dtos.updates.UpdatesDto;
import ru.otus.ms.core.updates.entities.UpdateHistory;
import ru.otus.ms.core.updates.entities.Updates;

@Mapper(componentModel = "spring")
public interface UpdatesMapper {

    UpdatesDto convertUpdatesToDto(Updates updates);

    UpdatesCreateRqDto convertNewUpdateToDto(Updates updates);

    Updates convertUpdatesToEntity(UpdatesCreateRqDto updatesCreateRqDto);

    UpdatedHistoryDto convertUpdatedHistoryToDto(UpdateHistory updateHistory);


}
