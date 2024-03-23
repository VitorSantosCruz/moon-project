CREATE TABLE `actions`(
  `id` SERIAL PRIMARY KEY,
  `user_id` BIGINT UNSIGNED,
  `created_date` TIMESTAMP NOT NULL,
  `type` VARCHAR(255) NOT NULL,
  `details` VARCHAR(255) NOT NULL,
  CONSTRAINT `fk_user_actions` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
)