package ru.otus.ms.core.updates.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "update_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHistory {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "original_update_id")
    private UUID originalUpdateId;
    @Column(name = "operation_system_area")
    private String place;
    @Column(name = "description")
    private String description;
    @Column(name = "changed_at")
    private Instant changeAt;
    @Column(name = "change_type")
    private String changeType;
}
