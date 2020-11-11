USE gift_certificate;

create table tag
(
    id_tag BIGINT NOT NULL AUTO_INCREMENT,
    name_tag VARCHAR(70) NOT NULL UNIQUE,
    lock_tag INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT pk_tag PRIMARY KEY (id_tag)
);

create table certificate
(
    id_certificate BIGINT NOT NULL AUTO_INCREMENT,
    name_certificate VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(300) NOT NULL,
    price DOUBLE NOT NULL,
    creation_date DATETIME NOT NULL,
    update_date DATETIME,
    lock_certificate INTEGER NOT NULL DEFAULT 0,
    duration INTEGER NOT NULL,

    CONSTRAINT pk_certificate PRIMARY KEY (id_certificate)
);

create table tag_certificate
(
    tag_id BIGINT NOT NULL,
    certificate_id BIGINT NOT NULL,

    CONSTRAINT pk_tag_certificate PRIMARY KEY (certificate_id, tag_id),
    CONSTRAINT fk_tag_certificate_tag_id FOREIGN KEY (tag_id)
        REFERENCES tag (id_tag) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_tag_certificate_certificate_id FOREIGN KEY (certificate_id)
        REFERENCES certificate (id_certificate) ON DELETE NO ACTION ON UPDATE NO ACTION
);

create table user
(
    id_user BIGINT NOT NULL AUTO_INCREMENT,
    name_user VARCHAR(70) NOT NULL UNIQUE,
    surname VARCHAR(70) NOT NULL UNIQUE,
    lock_user INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT pk_user PRIMARY KEY (id_user)
);

create table orders
(
    id_order BIGINT NOT NULL AUTO_INCREMENT,
    purchase_date DATETIME NOT NULL,
    total DOUBLE NOT NULL,
    lock_order INTEGER NOT NULL DEFAULT 0,
    id_user BIGINT NOT NULL,

    CONSTRAINT pk_orders PRIMARY KEY (id_order),
    CONSTRAINT fk_orders_id_user FOREIGN KEY (id_user)
        REFERENCES user (id_user) ON DELETE NO ACTION ON UPDATE NO ACTION
);

create table certificate_order
(
    order_id BIGINT NOT NULL,
    certificate_id BIGINT NOT NULL,

    CONSTRAINT pk_certificate_order PRIMARY KEY (order_id, certificate_id),
    CONSTRAINT fk_certificate_order_order_id FOREIGN KEY (order_id)
        REFERENCES orders (id_order) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_certificate_order_certificate_id FOREIGN KEY (certificate_id)
        REFERENCES certificate (id_certificate) ON DELETE NO ACTION ON UPDATE NO ACTION
);
