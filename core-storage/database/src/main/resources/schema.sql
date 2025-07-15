CREATE TABLE IF NOT EXISTS users
(
    user_id           BIGINT AUTO_INCREMENT,
    email             VARCHAR(255)                     NOT NULL,
    nickname          VARCHAR(20)                      NOT NULL,
    `role`            ENUM ('ROLE_USER', 'ROLE_ADMIN') NOT NULL,
    `password`        VARCHAR(255)                     NOT NULL,
    profile_image_url VARCHAR(255)                     NULL,
    created_at        DATETIME                         NOT NULL,
    updated_at        DATETIME                         NOT NULL,
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

CREATE TABLE IF NOT EXISTS `articles`
(
    article_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    `title`         VARCHAR(100) NOT NULL,
    `author`        VARCHAR(20)  NOT NULL,
    `description`   VARCHAR(200) NOT NULL,
    `thumbnail_url` VARCHAR(500)  NOT NULL,
    `link`          VARCHAR(500) NOT NULL,
    `category_id`   BIGINT       NOT NULL,
    `created_at`    DATETIME     NOT NULL,
    `updated_at`    DATETIME     NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories (category_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `tags`
(
    tag_id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`       VARCHAR(20) UNIQUE NOT NULL,
    `created_at` DATETIME           NOT NULL,
    `updated_at` DATETIME           NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `article_tags`
(
    article_id   BIGINT   NOT NULL,
    tag_id       BIGINT   NOT NULL,
    `created_at` DATETIME NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    FOREIGN KEY (article_id) REFERENCES articles (article_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (tag_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
