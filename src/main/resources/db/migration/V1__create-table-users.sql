CREATE TABLE `users`(
  `id` SERIAL PRIMARY KEY,
  `created_date` TIMESTAMP NOT NULL,
  `last_modified_date` TIMESTAMP,
  `name` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `deleted` BOOLEAN DEFAULT FALSE,
  `confirmed` BOOLEAN DEFAULT FALSE,
  `login_attempts` TINYINT NOT NULL DEFAULT 0,
  `blocked_until` TIMESTAMP
)
