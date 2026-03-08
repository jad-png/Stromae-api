CREATE DATABASE IF NOT EXISTS stromae_video_db;
CREATE DATABASE IF NOT EXISTS stromae_user_db;

USE stromae_video_db;

CREATE TABLE IF NOT EXISTS videos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description LONGTEXT,
    thumbnail_url VARCHAR(500),
    trailer_url VARCHAR(500),
    duration INT NOT NULL,
    release_year INT,
    type VARCHAR(50) NOT NULL,
    category VARCHAR(100) NOT NULL,
    rating DOUBLE,
    director VARCHAR(255),
    cast LONGTEXT,
    created_at BIGINT,
    updated_at BIGINT,
    INDEX idx_category (category),
    INDEX idx_type (type)
);

USE stromae_user_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at BIGINT,
    updated_at BIGINT,
    INDEX idx_username (username),
    INDEX idx_email (email)
);

CREATE TABLE IF NOT EXISTS watchlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    video_id BIGINT NOT NULL,
    added_at BIGINT,
    UNIQUE KEY unique_watchlist (user_id, video_id),
    INDEX idx_user_id (user_id),
    INDEX idx_video_id (video_id)
);

CREATE TABLE IF NOT EXISTS watch_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    video_id BIGINT NOT NULL,
    watched_at BIGINT,
    progress_time INT,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_user_id (user_id),
    INDEX idx_video_id (video_id),
    INDEX idx_user_video (user_id, video_id)
);
