drop all these tables
-- Enum type: status
CREATE TYPE status AS ENUM ('ONLINE', 'OFFLINE');

-- Table: users
CREATE TABLE users (
                       nick_name VARCHAR(255) PRIMARY KEY,
                       full_name VARCHAR(255),
                       status status -- using the enum type
);

-- Table: chat_room
CREATE TABLE chat_room (
                           id UUID PRIMARY KEY, -- Using UUID for better uniqueness
                           chat_id VARCHAR(255),
                           sender_id VARCHAR(255),
                           recipient_id VARCHAR(255),
                           FOREIGN KEY (sender_id) REFERENCES users(nick_name),
                           FOREIGN KEY (recipient_id) REFERENCES users(nick_name)
);

-- Table: chat_message
CREATE TABLE chat_message (
                              id UUID PRIMARY KEY, -- Using UUID for better uniqueness
                              chat_id VARCHAR(255),
                              sender_id VARCHAR(255),
                              recipient_id VARCHAR(255),
                              content TEXT,
                              time_stamp TIMESTAMP,
                              FOREIGN KEY (sender_id) REFERENCES users(nick_name),
                              FOREIGN KEY (recipient_id) REFERENCES users(nick_name)
);



ALTER TABLE users ADD COLUMN profile_image TEXT;

ALTER TABLE users
    RENAME COLUMN nick_name TO user_name;

ALTER TABLE users
    RENAME COLUMN full_name TO email;



TRUNCATE TABLE chat_message, chat_room, users CASCADE;

TRUNCATE TABLE chat_message;

TRUNCATE TABLE chat_room;



--AlterNate Query

-- Recreate users table
CREATE TABLE users (
                       user_name VARCHAR(255) PRIMARY KEY,
                       email VARCHAR(255),
                       profile_image TEXT,
                       status status
);

-- Recreate chat_room table
CREATE TABLE chat_room (
                           id UUID PRIMARY KEY,
                           chat_id VARCHAR(255),
                           sender_id VARCHAR(255),
                           recipient_id VARCHAR(255),
                           FOREIGN KEY (sender_id) REFERENCES users(user_name),
                           FOREIGN KEY (recipient_id) REFERENCES users(user_name)
);

-- Recreate chat_message table
CREATE TABLE chat_message (
                              id UUID PRIMARY KEY,
                              chat_id VARCHAR(255),
                              sender_id VARCHAR(255),
                              recipient_id VARCHAR(255),
                              content TEXT,
                              time_stamp TIMESTAMP,
                              FOREIGN KEY (sender_id) REFERENCES users(user_name),
                              FOREIGN KEY (recipient_id) REFERENCES users(user_name)
);
