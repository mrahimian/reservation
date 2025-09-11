-- users table
CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- available_slots table
CREATE TABLE available_slots
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time  DATETIME NOT NULL,
    end_time    DATETIME NOT NULL,
    is_reserved BOOLEAN DEFAULT FALSE
);
-- Index to quickly find available slots. Order by start_time where is_reserved is false
CREATE INDEX reserved_start_idx ON available_slots (is_reserved, start_time);

-- reservations table
CREATE TABLE reservations
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    slot_id     BIGINT NOT NULL UNIQUE, -- Unique because a slot can not be reserved more than once
    reserved_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (slot_id) REFERENCES available_slots (id)
);

-- Users with hashed password (all passwords: 123456)
-- Hashed using BCryptPasswordEncoder
INSERT INTO users (username, email, password)
VALUES ('user1', 'user1@example.com', '$2b$10$eY5yMasvyG8PWVltMFWUMuuFrg3IUdcX9Y67C4sKLINKryYdJgx12'),
       ('user2', 'user2@example.com', '$2b$10$Pm3pFL1MEScGGmWdJfP26.OzJqHcQPoSj1LstSnZARd77/.2MZW1m'),
       ('user3', 'user3@example.com', '$2b$10$vAPqN/voYK8pzXxpd8Hp6evYBTzXDUmnvZ06a0.MI4MINnfYToUea');

-- Available slots
INSERT INTO available_slots (start_time, end_time)
VALUES ('2024-12-29 09:00:00', '2024-12-29 10:00:00'),
       ('2024-12-29 10:00:00', '2024-12-29 11:00:00'),
       ('2024-12-29 11:00:00', '2024-12-29 12:00:00'),
       ('2024-12-29 12:00:00', '2024-12-29 13:00:00'),
       ('2024-12-29 13:00:00', '2024-12-29 14:00:00'),
       ('2024-12-29 14:00:00', '2024-12-29 15:00:00'),
       ('2024-12-29 15:00:00', '2024-12-29 16:00:00'),
       ('2024-12-29 16:00:00', '2024-12-29 17:00:00'),
       ('2024-12-30 09:00:00', '2024-12-30 10:00:00'),
       ('2024-12-30 10:00:00', '2024-12-30 11:00:00');
