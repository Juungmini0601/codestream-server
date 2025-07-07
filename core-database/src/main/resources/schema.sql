CREATE TABLE IF NOT EXISTS users
(
    user_id    BIGINT AUTO_INCREMENT,
    email      VARCHAR(255)                     NOT NULL,
    nickname   VARCHAR(20)                      NOT NULL,
    `role`     ENUM ('ROLE_USER', 'ROLE_ADMIN') NOT NULL,
    password   VARCHAR(255)                     NOT NULL,
    created_at DATETIME                         NOT NULL,
    updated_at DATETIME                         NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE KEY unique_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `oauths`
(
    `oauthId`     BIGINT PRIMARY KEY AUTO_INCREMENT,
    `provider`    ENUM ('GOOGLE', 'KAKAO') NOT NULL,
    `provider_id` VARCHAR(255)             NOT NULL,
    `user_id`     BIGINT                   NOT NULL,
    `created_at`  DATETIME                 NOT NULL,
    `updated_at`  DATETIME                 NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
    CONSTRAINT `uk_provider_providerid` UNIQUE (`provider`, `provider_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `categories`
(
    category_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`       VARCHAR(100) UNIQUE NOT NULL,
    `created_at` DATETIME            NOT NULL,
    `updated_at` DATETIME            NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
