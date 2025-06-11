DROP DATABASE IF EXISTS `jdr_generator_db`;
CREATE DATABASE IF NOT EXISTS `jdr_generator_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
USE `jdr_generator_db`;

-- Suppression des tables existantes pour une recréation propre
-- L'ordre est important pour les clés étrangères
DROP TABLE IF EXISTS `jdr_generator_db`.`character_illustration`;
DROP TABLE IF EXISTS `jdr_generator_db`.`character_json_data`;
DROP TABLE IF EXISTS `jdr_generator_db`.`character_details`;
DROP TABLE IF EXISTS `jdr_generator_db`.`character_context`;

-- Table character_context (parent de character_details)
CREATE TABLE `jdr_generator_db`.`character_context` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `prompt_system` VARCHAR(255) DEFAULT NULL,
    `prompt_race` VARCHAR(255) DEFAULT NULL,
    `prompt_gender` VARCHAR(255) DEFAULT NULL,
    `prompt_class` VARCHAR(255) DEFAULT NULL,
    `prompt_description` TEXT DEFAULT NULL,
    `prompt_draw_style` VARCHAR(255) NOT NULL DEFAULT 'photoRealistic', -- Ajout de V00003
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- Table character_details
CREATE TABLE `jdr_generator_db`.`character_details` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `context_id` INT NOT NULL DEFAULT 1,
    `name` VARCHAR(255) DEFAULT NULL,
    `age` INTEGER DEFAULT NULL,
    `birth_place` TEXT DEFAULT NULL,
    `residence_location` TEXT DEFAULT NULL,
    `reason_for_residence` TEXT DEFAULT NULL,
    `climate` TEXT DEFAULT NULL,
    `common_problems` TEXT DEFAULT NULL,
    `daily_routine` TEXT DEFAULT NULL,
    `parents_alive` BOOLEAN DEFAULT NULL,
    `details_about_parents` TEXT DEFAULT NULL,
    `feelings_about_parents` TEXT DEFAULT NULL,
    `siblings` TEXT DEFAULT NULL,
    `childhood_story` TEXT DEFAULT NULL,
    `youth_friends` TEXT DEFAULT NULL,
    `pet` TEXT DEFAULT NULL,
    `marital_status` TEXT DEFAULT NULL,
    `type_of_lover` TEXT DEFAULT NULL,
    `conjugal_history` TEXT DEFAULT NULL,
    `children` TEXT DEFAULT NULL,
    `education` TEXT DEFAULT NULL,
    `profession` TEXT DEFAULT NULL,
    `reason_for_profession` TEXT DEFAULT NULL,
    `work_preferences` TEXT DEFAULT NULL,
    `change_in_world` TEXT DEFAULT NULL,
    `change_in_self` TEXT DEFAULT NULL,
    `goal` TEXT DEFAULT NULL,
    `reason_for_goal` TEXT DEFAULT NULL,
    `biggest_obstacle` TEXT DEFAULT NULL,
    `overcoming_obstacle` TEXT DEFAULT NULL,
    `plan_if_successful` TEXT DEFAULT NULL,
    `plan_if_failed` TEXT DEFAULT NULL,
    `self_description` TEXT DEFAULT NULL,
    `distinctive_trait` TEXT DEFAULT NULL,
    `physical_description` TEXT DEFAULT NULL,
    `clothing_preferences` TEXT DEFAULT NULL,
    `fears` TEXT DEFAULT NULL,
    `favorite_food` TEXT DEFAULT NULL,
    `hobbies` TEXT DEFAULT NULL,
    `leisure_activities` TEXT DEFAULT NULL,
    `ideal_company` TEXT DEFAULT NULL,
    `attitude_towards_groups` TEXT DEFAULT NULL,
    `attitude_towards_world` TEXT DEFAULT NULL,
    `attitude_towards_people` TEXT DEFAULT NULL,
    `image` TEXT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_character_context_id`
        FOREIGN KEY (`context_id`)
            REFERENCES character_context(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- Table character_illustration (enfant de character_details)
CREATE TABLE `jdr_generator_db`.`character_illustration` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `character_details_id` INT NOT NULL,
    `image_label` TEXT DEFAULT NULL,
    `image_blob` LONGBLOB DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_character_details_image`
        FOREIGN KEY (`character_details_id`)
            REFERENCES `character_details`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- Table character_json_data (enfant de character_details)
CREATE TABLE `jdr_generator_db`.`character_json_data` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `character_details_id` INT NOT NULL,
    `json_data` JSON NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_character_details_json_data`
        FOREIGN KEY (`character_details_id`)
            REFERENCES `character_details`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;