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