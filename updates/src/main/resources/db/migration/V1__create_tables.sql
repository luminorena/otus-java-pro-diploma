CREATE TABLE updates
(
    id                    UUID PRIMARY KEY,
    operation_system_area VARCHAR(50)  NOT NULL,
    description           varchar(500) NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_updates_os_area ON updates (operation_system_area);

CREATE TABLE update_history
(
    id                    UUID PRIMARY KEY,
    original_update_id    UUID,
    operation_system_area VARCHAR(50),
    description           varchar(500) NOT NULL,
    changed_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    change_type           VARCHAR(10)  NOT NULL CHECK (change_type IN ('create', 'update', 'delete'))
);


CREATE TABLE users
(
    id    UUID PRIMARY KEY,
    fio   varchar(255),
    email VARCHAR(120)
);



CREATE TABLE user_updates
(
    id        UUID PRIMARY KEY,
    user_id   UUID REFERENCES users (id),
    update_id UUID REFERENCES updates (id)
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
