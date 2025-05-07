CREATE DATABASE IF NOT EXISTS `jdr_generator_db`;
#ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';

DROP TABLE IF EXISTS `character_illustration`, `character_details`, `character_context`, `character_json_data` CASCADE;

DROP TABLE IF EXISTS `jdr_generator_db`.`character_context`;
CREATE TABLE `jdr_generator_db`.`character_context` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `prompt_system` VARCHAR(255) DEFAULT NULL,
    `prompt_race` VARCHAR(255) DEFAULT NULL,
    `prompt_gender` VARCHAR(255) DEFAULT NULL,
    `prompt_class` VARCHAR(255) DEFAULT NULL,
    `prompt_description` TEXT DEFAULT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

INSERT INTO `jdr_generator_db`.`character_context` (`id`, `prompt_system`, `prompt_race`, `prompt_gender`, `prompt_class`, `prompt_description`)
VALUES (1, 'Système de jeu non défini', '', 'Aucun genre spécifié', '', '');

DROP TABLE IF EXISTS `jdr_generator_db`.`character_details`;
CREATE TABLE `jdr_generator_db`.`character_details` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255)  DEFAULT NULL,
    `age` INTEGER NOT NULL,
    `birth_place` TEXT  NOT NULL,
    `residence_location` TEXT  NOT NULL,
    `reason_for_residence` TEXT ,
    `climate` TEXT ,
    `common_problems` TEXT ,
    `daily_routine` TEXT ,
    `parents_alive` BOOLEAN NOT NULL,
    `details_about_parents` TEXT ,
    `feelings_about_parents` TEXT  NOT NULL,
    `siblings` TEXT ,
    `childhood_story` TEXT  NOT NULL,
    `youth_friends` TEXT,
    `pet` TEXT ,
    `marital_status` TEXT  NOT NULL,
    `type_of_lover` TEXT ,
    `conjugal_history` TEXT ,
    `children` TEXT ,
    `education` TEXT  NOT NULL,
    `profession` TEXT  NOT NULL,
    `reason_for_profession` TEXT  NOT NULL,
    `work_preferences` TEXT ,
    `change_in_world` TEXT ,
    `change_in_self` TEXT ,
    `goal` TEXT ,
    `reason_for_goal` TEXT ,
    `biggest_obstacle` TEXT ,
    `overcoming_obstacle` TEXT ,
    `plan_if_successful` TEXT ,
    `plan_if_failed` TEXT ,
    `self_description` TEXT ,
    `distinctive_trait` TEXT  NOT NULL,
    `physical_description` TEXT  NOT NULL,
    `clothing_preferences` TEXT ,
    `fears` TEXT ,
    `favorite_food` TEXT ,
    `hobbies` TEXT ,
    `leisure_activities` TEXT ,
    `ideal_company` TEXT ,
    `attitude_towards_groups` TEXT ,
    `attitude_towards_world` TEXT  NOT NULL,
    `attitude_towards_people` TEXT  NOT NULL,
    `image` TEXT  NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `context_id` INT NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_character_context_id`
        FOREIGN KEY (`context_id`)
            REFERENCES character_context(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `jdr_generator_db`.`character_illustration`;
CREATE TABLE `jdr_generator_db`.`character_illustration` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `image_label` TEXT,
     `image_blob` LONGBLOB,
     `image_details` INT NOT NULL,
     PRIMARY KEY (`id`),
     FOREIGN KEY (`image_details`) REFERENCES `character_details`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;