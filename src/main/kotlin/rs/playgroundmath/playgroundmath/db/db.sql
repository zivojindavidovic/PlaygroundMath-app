CREATE TABLE `role` (
                        `role_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                        `role_type` ENUM('PARENT', 'TEACHER', 'ADMIN') NOT NULL
);

CREATE TABLE `user` (
                        `user_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                        `email` VARCHAR(255) NOT NULL UNIQUE,
                        `password` VARCHAR(255) NOT NULL,
                        `status` ENUM('ACTIVE', 'PENDING', 'SUSPENDED', 'DELETED') NOT NULL DEFAULT 'PENDING',
                        `role_id` BIGINT NOT NULL,
                        FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE `course` (
                          `course_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                          `age` INT NOT NULL,
                          `due_date` DATETIME NULL,
                          `user_id` BIGINT NULL,
                          FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE `account` (
                           `account_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                           `username` VARCHAR(255) NOT NULL,
                           `points` BIGINT NOT NULL DEFAULT 0,
                           `age` INT NOT NULL,
                           `user_id` BIGINT NOT NULL,
                           FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `account_course` (
                                  `account_id` BIGINT NOT NULL,
                                  `course_id` BIGINT NOT NULL,
                                  PRIMARY KEY (`account_id`, `course_id`),
                                  FOREIGN KEY (`account_id`) REFERENCES `account`(`account_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                  FOREIGN KEY (`course_id`) REFERENCES `course`(`course_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `test` (
                        `test_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                        `course_id` BIGINT NULL,
                        `account_id` BIGINT NULL,
                        `is_completed` ENUM('YES', 'NO') NULL DEFAULT NULL,
                        FOREIGN KEY (`course_id`) REFERENCES `course`(`course_id`) ON DELETE SET NULL ON UPDATE CASCADE,
                        FOREIGN KEY (`account_id`) REFERENCES `account`(`account_id`) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE `task` (
                        `task_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                        `first_number` VARCHAR(255) NOT NULL,
                        `second_number` VARCHAR(255) NOT NULL,
                        `operation` VARCHAR(255) NOT NULL,
                        `result` VARCHAR(255) NOT NULL,
                        `points` INT NOT NULL,
                        `test_id` BIGINT NOT NULL,
                        FOREIGN KEY (`test_id`) REFERENCES `test`(`test_id`) ON DELETE CASCADE ON UPDATE CASCADE
);
