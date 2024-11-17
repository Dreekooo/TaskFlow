CREATE TABLE `Projects`(
    `project_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `created_by` INT NOT NULL,
    `created_at` TIMESTAMP NOT NULL
);
CREATE TABLE `TaskComments`(
    `comment_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `task_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `content` TEXT NOT NULL,
    `created_at` TIMESTAMP NOT NULL
);
CREATE TABLE `Priorities`(
    `priority_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `priority_name` VARCHAR(255) NOT NULL
);
CREATE TABLE `Users`(
    `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL,
    `username` VARCHAR(255) NOT NULL,
    `first_name` VARCHAR(255) NOT NULL,
    `last_name` VARCHAR(255) NOT NULL,
    `password_hash` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL
);
CREATE TABLE `Tasks`(
    `task_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `project_id` INT NOT NULL,
    `assigned_to` INT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `status` INT NOT NULL,
    `created_by` INT NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    `priority` INT NOT NULL,
    `start_date` DATE NOT NULL,
    `due_date` DATE NOT NULL
);
CREATE TABLE `Status`(
    `status_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `status_name` VARCHAR(255) NOT NULL
);
CREATE TABLE `Roles`(
    `role_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `role_name` VARCHAR(255) NOT NULL
);
CREATE TABLE `ProjectUsers`(
    `project_user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `project_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `role` INT NOT NULL
);
ALTER TABLE
    `Tasks` ADD CONSTRAINT `tasks_status_foreign` FOREIGN KEY(`status`) REFERENCES `Status`(`status_id`);
ALTER TABLE
    `Tasks` ADD CONSTRAINT `tasks_project_id_foreign` FOREIGN KEY(`project_id`) REFERENCES `Projects`(`project_id`);
ALTER TABLE
    `ProjectUsers` ADD CONSTRAINT `projectusers_role_foreign` FOREIGN KEY(`role`) REFERENCES `Roles`(`role_id`);
ALTER TABLE
    `Projects` ADD CONSTRAINT `projects_created_by_foreign` FOREIGN KEY(`created_by`) REFERENCES `Users`(`user_id`);
ALTER TABLE
    `Tasks` ADD CONSTRAINT `tasks_assigned_to_foreign` FOREIGN KEY(`assigned_to`) REFERENCES `Users`(`user_id`);
ALTER TABLE
    `TaskComments` ADD CONSTRAINT `taskcomments_task_id_foreign` FOREIGN KEY(`task_id`) REFERENCES `Tasks`(`task_id`);
ALTER TABLE
    `ProjectUsers` ADD CONSTRAINT `projectusers_project_id_foreign` FOREIGN KEY(`project_id`) REFERENCES `Projects`(`project_id`);
ALTER TABLE
    `ProjectUsers` ADD CONSTRAINT `projectusers_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `Users`(`user_id`);
ALTER TABLE
    `Tasks` ADD CONSTRAINT `tasks_created_by_foreign` FOREIGN KEY(`created_by`) REFERENCES `Users`(`user_id`);
ALTER TABLE
    `TaskComments` ADD CONSTRAINT `taskcomments_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `Users`(`user_id`);
ALTER TABLE
    `Tasks` ADD CONSTRAINT `tasks_priority_foreign` FOREIGN KEY(`priority`) REFERENCES `Priorities`(`priority_id`);