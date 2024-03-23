CREATE TABLE `users_roles`(
  `users_id` BIGINT UNSIGNED NOT NULL,
  `roles_id` BIGINT UNSIGNED NOT NULL,
  CONSTRAINT `pk_users_roles` PRIMARY KEY (`users_id`, `roles_id`),
  CONSTRAINT `fk_user_users_roles` FOREIGN KEY (`users_id`) REFERENCES `users`(`id`),
  CONSTRAINT `fk_role_users_roles` FOREIGN KEY (`roles_id`) REFERENCES `roles`(`id`)
)
