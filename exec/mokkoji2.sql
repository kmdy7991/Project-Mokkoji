-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema mokkojiusersusers
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mokkoji
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mokkoji` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `mokkoji` ;

-- -----------------------------------------------------
-- Table `mokkoji`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mokkoji`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `user_nickname` VARCHAR(16) NOT NULL,
  `email` VARCHAR(64) NULL DEFAULT NULL,
  `provider` ENUM('GOOGLE', 'NAVER', 'KAKAO') NULL DEFAULT NULL,
  `role` ENUM('USER', 'ADMIN', 'GUEST') NULL DEFAULT NULL,
  `refresh_token` VARCHAR(256) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `oauth2Id` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_cr59axqya8utby3j37qi341rm` (`user_nickname` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_users_nickname_id` (`id` ASC, `user_nickname` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mokkoji`.`game_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mokkoji`.`game_data` (
  `win_count` INT NOT NULL DEFAULT '0',
  `game_count` INT NOT NULL DEFAULT '0',
  `user_rp` INT NOT NULL DEFAULT '0' COMMENT '랭킹포인트로 바꿔야할듯',
  `user_ranking_badge` VARCHAR(25) NULL DEFAULT NULL,
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_game_data_users1`
    FOREIGN KEY (`id`)
    REFERENCES `mokkoji`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mokkoji`.`gameroom`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mokkoji`.`gameroom` (
  `room_id` INT NOT NULL AUTO_INCREMENT,
  `room_name` VARCHAR(50) NULL DEFAULT '',
  `room_password` VARCHAR(8) NULL DEFAULT '',
  `user_count` INT NULL DEFAULT NULL,
  `is_private` TINYINT(1) NOT NULL DEFAULT '0',
  `is_explosion` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '방폭파됐냐 안됐냐',
  `is_active` TINYINT(1) NOT NULL DEFAULT '0' COMMENT 'True 시작 False 시작 ㄴㄴ',
  `game_type` INT NOT NULL DEFAULT '0' COMMENT '0이면 몸으로 말해요, 1이면 뮤직큐',
  `owner` VARCHAR(16) NULL DEFAULT NULL COMMENT '생성자가 방장이 되도록',
  `turn_num` INT NULL DEFAULT '0' COMMENT '몇 번 유저 차례인지',
  PRIMARY KEY (`room_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 29
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mokkoji`.`participant`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mokkoji`.`participant` (
  `room_id` INT NOT NULL,
  `id` INT NOT NULL,
  `score` INT NULL DEFAULT 0,
  `user_nickname` VARCHAR(45) NOT NULL,
  UNIQUE INDEX `user_nickname_UNIQUE` (`user_nickname` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_participant_gameroom_idx` (`room_id` ASC) VISIBLE,
  INDEX `fk_participant_users1_idx` (`id` ASC, `user_nickname` ASC) VISIBLE,
  CONSTRAINT `fk_participant_gameroom`
    FOREIGN KEY (`room_id`)
    REFERENCES `mokkoji`.`gameroom` (`room_id`),
  CONSTRAINT `fk_participant_users1`
    FOREIGN KEY (`id` , `user_nickname`)
    REFERENCES `mokkoji`.`users` (`id` , `user_nickname`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
