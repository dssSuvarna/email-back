CREATE TABLE IF NOT EXISTS auth_permission
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS auth_role
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS auth_role_permission
(
    role_id       BIGINT,
    permission_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES auth_role (id),
    FOREIGN KEY (permission_id) REFERENCES auth_permission (id),
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE IF NOT EXISTS auth_user
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    status       VARCHAR(50)  NOT NULL,
    auth_role_id BIGINT,
    FOREIGN KEY (auth_role_id) REFERENCES auth_role (id)
);

INSERT INTO auth_role(name)
VALUES ('ADMIN');

INSERT INTO auth_user(username, password, status, auth_role_id)
VALUES ('admin@gmail.com', '$2a$12$N.TWOz3HBA8AKI.rjmlmV.pqEpwj0l0bmdw08qUhrDE0f.JDGa0Mi', 'ENABLED', 1);

CREATE TABLE IF NOT EXISTS campaign
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    status          VARCHAR(50)  NOT NULL,
    schedule_time    DATETIME,
    end_time         DATETIME,
    templates       JSON,
    contacts        JSON,
    mailed_contact   JSON,
    campaign_senders JSON,
    created_by       BIGINT       NOT NULL
);

CREATE TABLE IF NOT EXISTS contact
(
    id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(512) NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    company VARCHAR(512) NULL,
    group_name VARCHAR(512) NOT NULL,
    is_opened BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS sender
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    mail       VARCHAR(255) NOT NULL,
    port       INT NOT NULL,
    host       VARCHAR(255) NOT NULL,
    pass_key    VARCHAR(255) NOT NULL,
    user_id    BIGINT NOT NULL ,
    signatures JSON
);

CREATE TABLE IF NOT EXISTS template
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    content TEXT,
    campaign_id BIGINT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS user
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    auth_user_id BIGINT       NOT NULL,
    status       VARCHAR(50)  NOT NULL,
    FOREIGN KEY (auth_user_id) REFERENCES auth_user (id)
);

CREATE TABLE notification
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheduled_time DATETIME     NOT NULL,
    campaign_id    BIGINT       NOT NULL,
    no_of_retries  INT DEFAULT 0,
    status         VARCHAR(255) NOT NULL,
    sender_id      BIGINT       NOT NULL,
    contact_id     BIGINT       NOT NULL,
    template_id    BIGINT       NOT NULL,
    CONSTRAINT fk_campaign FOREIGN KEY (campaign_id) REFERENCES campaign (id),
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES sender (id),
    CONSTRAINT fk_contact FOREIGN KEY (contact_id) REFERENCES contact (id),
    CONSTRAINT fk_template FOREIGN KEY (template_id) REFERENCES template (id)
);

INSERT INTO user(first_name, last_name, auth_user_id, status)
VALUES ('admin', '', 1, 'CREATED');




