package ru.otus.updates.consumer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.updates.consumer.entities.Users;

import java.util.List;
import java.util.UUID;


@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

    @Query("SELECT distinct userEmail from Users")
    List<String> findUserEmails();


}
