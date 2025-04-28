package ru.otus.ms.core.updates.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdates {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "update_id")
    private UUID updateId;
}
