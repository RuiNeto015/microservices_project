CREATE TABLE product_postgresql
(
    sku               VARCHAR(255)     NOT NULL,
    approval_status   VARCHAR(255)     NOT NULL,
    designation       VARCHAR(255)     NOT NULL,
    description       VARCHAR(255)     NOT NULL,
    aggregated_rating DOUBLE PRECISION NOT NULL,
    num_approvals     INTEGER          NOT NULL,
    CONSTRAINT pk_product_postgresql PRIMARY KEY (sku)
);

CREATE TABLE review_postgresql
(
    id              VARCHAR(255)     NOT NULL,
    approval_status VARCHAR(255)     NOT NULL,
    text            VARCHAR(255)     NOT NULL,
    report          VARCHAR(255),
    publishing_date date             NOT NULL,
    fun_fact        VARCHAR(255)     NOT NULL,
    product_sku     VARCHAR(255)     NOT NULL,
    user_id         VARCHAR(255)     NOT NULL,
    rating          DOUBLE PRECISION NOT NULL,
    num_approvals   INTEGER          NOT NULL,
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

ALTER TABLE review_postgresql
    ADD CONSTRAINT FK_REVIEW_POSTGRESQL_ON_PRODUCT_SKU FOREIGN KEY (product_sku) REFERENCES product_postgresql (sku);

ALTER TABLE review_postgresql
    ADD CONSTRAINT FK_REVIEW_POSTGRESQL_ON_USER FOREIGN KEY (user_id) REFERENCES user_postgresql (id);

ALTER TABLE user_postgresql
    ADD CONSTRAINT uc_user_postgresql_email UNIQUE (email);

ALTER TABLE vote_postgresql
    ADD CONSTRAINT FK_VOTE_POSTGRESQL_ON_PRODUCT FOREIGN KEY (review_id) REFERENCES review_postgresql (id);

ALTER TABLE vote_postgresql
    ADD CONSTRAINT FK_VOTE_POSTGRESQL_ON_USER FOREIGN KEY (user_id) REFERENCES user_postgresql (id);