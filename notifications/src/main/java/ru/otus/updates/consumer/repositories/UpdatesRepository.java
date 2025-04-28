package ru.otus.updates.consumer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.otus.updates.consumer.entities.Updates;

import java.util.Optional;
import java.util.UUID;

public interface UpdatesRepository extends JpaRepository<Updates, UUID> {

    Optional<Updates> findById(@NonNull UUID id);


}
