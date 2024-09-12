CREATE TABLE review_postgresql
(
    id              VARCHAR(255) NOT NULL,
    approval_status VARCHAR(255) NOT NULL,
    user_id         VARCHAR(255) NOT NULL,
    num_approvals   INTEGER      NOT NULL,
    CONSTRAINT pk_review_postgresql PRIMARY KEY (id)
);

CREATE TABLE user_postgresql
(
    id        VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role      VARCHAR(255) NOT NULL,
    nif       VARCHAR(255) NOT NULL,
    address   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_postgresql PRIMARY KEY (id)
);

CREATE TABLE vote_postgresql
(
    user_id   VARCHAR(255) NOT NULL,
    vote      VARCHAR(255) NOT NULL,
    review_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_vote_postgresql PRIMARY KEY (user_id, review_id)
);

ALTER TABLE user_postgresql
    ADD CONSTRAINT uc_user_postgresql_email UNIQUE (email);

ALTER TABLE review_postgresql
    ADD CONSTRAINT FK_REVIEW_POSTGRESQL_ON_USER FOREIGN KEY (user_id) REFERENCES user_postgresql (id);

ALTER TABLE vote_postgresql
    ADD CONSTRAINT FK_VOTE_POSTGRESQL_ON_REVIEW FOREIGN KEY (review_id) REFERENCES review_postgresql (id);

ALTER TABLE vote_postgresql
    ADD CONSTRAINT FK_VOTE_POSTGRESQL_ON_USER FOREIGN KEY (user_id) REFERENCES user_postgresql (id);