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