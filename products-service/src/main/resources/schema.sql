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

ALTER TABLE user_postgresql
    ADD CONSTRAINT uc_user_postgresql_email UNIQUE (email);

