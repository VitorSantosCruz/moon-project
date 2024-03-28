CREATE TABLE `users_roles`(
  `user_id` BIGINT UNSIGNED NOT NULL,
  `role_id` BIGINT UNSIGNED NOT NULL,
  CONSTRAINT `pk_users_roles` PRIMARY KEY (`user_id`, `role_id`),
  CONSTRAINT `fk_user_users_roles` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
  CONSTRAINT `fk_role_users_roles` FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`)
)
