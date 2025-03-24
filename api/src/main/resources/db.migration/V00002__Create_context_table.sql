DROP TABLE IF EXISTS `jdr_generator_db`.`context`;
CREATE TABLE `jdr_generator_db`.`context` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `promptSystem` VARCHAR(255) DEFAULT NULL,
    `promptRace` VARCHAR(255) DEFAULT NULL,
    `promptClass` VARCHAR(255) DEFAULT NULL,
    `promptDescription` TEXT DEFAULT NULL,
    `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


ALTER TABLE `jdr_generator_db`.`character`
    ADD `context_id` INT UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE `jdr_generator_db`.`character`
    ADD CONSTRAINT `fk_context_id`
        FOREIGN KEY (`context_id`)
        REFERENCES context(`id`);