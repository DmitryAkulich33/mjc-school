INSERT INTO tag (name_tag)
VALUES ('food'),
       ('delivery');
INSERT INTO certificate (name_certificate, description, price, creation_date, update_date, duration)
VALUES ('Certificate for one purchase', 'Certificate for one going to the shop', 50.0, '2020-10-22T11:45:11', null, 365),
       ('Certificate for dinner in a restaurant', 'Food and drink without check limit at Viet Express', 100.0,
        '2020-11-22T12:45:11', null, 100);
INSERT INTO tag_certificate (tag_id, certificate_id)
VALUES (1, 1),
       (1, 2),
       (2, 1);
INSERT INTO user (name_user, surname, lock_user)
VALUES ('Ivan', 'Ivanov', 0),
       ('Petr', 'Petrov', 0);
INSERT INTO orders (purchase_date, total, id_user)
VALUES ('2020-11-22T12:45:11', 150, 1),
       ('2020-11-22T12:45:11', 50, 2);
INSERT INTO certificate_order (order_id, certificate_id)
VALUES (1, 1),
       (1, 2),
       (2, 1);