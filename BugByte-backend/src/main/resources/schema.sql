CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    bio TEXT,
    reputation BIGINT DEFAULT 0,
    is_admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS validation_codes (
    user_id BIGINT NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS followers (
    follower_id BIGINT NOT NULL,
    followed_id BIGINT NOT NULL,
    PRIMARY KEY (follower_id, followed_id),
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (followed_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS communities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    admin_id BIGINT NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS community_members (
    community_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (community_id, member_id),
    FOREIGN KEY (community_id) REFERENCES communities(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    op_name VARCHAR(255) NOT NULL,
    md_content TEXT NOT NULL,
    posted_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (op_name) REFERENCES users(user_name) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS questions (
    id BIGINT PRIMARY KEY,
    community_id BIGINT NOT NULL,
    up_votes BIGINT DEFAULT 0,
    down_votes BIGINT DEFAULT 0,
    validated_answer_id BIGINT,
    FOREIGN KEY (id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (community_id) REFERENCES communities(id) ON DELETE CASCADE,
    FOREIGN KEY (validated_answer_id) REFERENCES posts(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS answers (
    id BIGINT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    up_votes BIGINT DEFAULT 0,
    down_votes BIGINT DEFAULT 0,
    FOREIGN KEY (id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS replies (
    id BIGINT PRIMARY KEY,
    answer_id BIGINT NOT NULL,
    FOREIGN KEY (id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (answer_id) REFERENCES answers(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS question_tag (
    question_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (question_id, tag_id),
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (tag_id) REFERENCES tag(id)
);

CREATE TABLE IF NOT EXISTS community_tag (
    community_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (community_id, tag_id),
    FOREIGN KEY (community_id) REFERENCES communities(id),
    FOREIGN KEY (tag_id) REFERENCES tag(id)
);

CREATE TABLE IF NOT EXISTS moderators (
    id BIGINT NOT NULL,
    community_id BIGINT NOT NULL,
    PRIMARY KEY (id, community_id),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (community_id) REFERENCES communities(id) ON DELETE CASCADE
);