USE gift_certificate;

create table tag
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name_tag VARCHAR(70) NOT NULL UNIQUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_tag PRIMARY KEY (id)
);

create table certificate
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name_certificate VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(300) NOT NULL,
    price DOUBLE NOT NULL,
    creation_date DATETIME NOT NULL,
    update_date DATETIME,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    duration INTEGER NOT NULL,

    CONSTRAINT pk_certificate PRIMARY KEY (id)
);

create table tag_certificate
(
    tag_id BIGINT NOT NULL,
    certificate_id BIGINT NOT NULL,

    CONSTRAINT pk_tag_certificate PRIMARY KEY (certificate_id, tag_id),
    CONSTRAINT fk_tag_certificate_tag_id FOREIGN KEY (tag_id)
        REFERENCES tag (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_tag_certificate_certificate_id FOREIGN KEY (certificate_id)
        REFERENCES certificate (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

create table role
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name_role VARCHAR(70) NOT NULL,

    CONSTRAINT pk_role PRIMARY KEY (id)
);

create table user
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(70) NOT NULL UNIQUE,
    password VARCHAR(70) NOT NULL,
    name_user VARCHAR(70) NOT NULL,
    surname VARCHAR(70) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_user PRIMARY KEY (id)
);

create table user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id)
        REFERENCES user (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id)
        REFERENCES role (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

create table orders
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    purchase_date DATETIME NOT NULL,
    total DOUBLE NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    id_user BIGINT NOT NULL,

    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT fk_orders_id_user FOREIGN KEY (id_user)
        REFERENCES user (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

create table certificate_order
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    certificate_id BIGINT NOT NULL,

    CONSTRAINT pk_certificate_order PRIMARY KEY (id),
    CONSTRAINT fk_certificate_order_order_id FOREIGN KEY (order_id)
        REFERENCES orders (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_certificate_order_certificate_id FOREIGN KEY (certificate_id)
        REFERENCES certificate (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
