USE gift_certificate;

INSERT INTO tag
(name_tag)
VALUES ("food"),
       ("delivery"),
       ("jewellery"),
       ("beauty"),
       ("restaurant"),
       ("entertainment"),
       ("spa"),
       ("tour");

INSERT INTO certificate
(name_certificate, description, price, creation_date, duration)
VALUES ("Certificate for ordering food delivery", "Buffet set of oriental dishes from an off-site restaurant", 25.00,
        "2020-10-22T10:00:09", 90),
       ("Jewelry purchase certificate",
        "The certificate gives the right to purchase any jewelry for women and men in the 7 CARAT salon for the amount of 500 rubles",
        500.00, "2020-10-22T11:00:09", 365),
       ("Certificate for visiting a beauty salon",
        "Facial cleansing, peeling with application of a mask or a comprehensive Anti-age program in the Beauty Salon and SPA",
        100.00, "2020-10-22T11:30:09", 120),
       ("Certificate for dinner in a restaurant", "Food and drink without check limit at Viet Express", 80.00,
        "2020-10-22T11:45:11", 90),
       ("SPA certificate", "Romantic SPA date for two any day", 110.00, "2020-10-22T11:55:11", 200),
       ("Certificate for a guided tour of your choice",
        "Guided tour of Antalya, Pamukkale, Ephesus, Pergamum and Three for 1 person",
        300.00, "2020-10-22T12:00:15", 365);

INSERT INTO tag_certificate
(tag_id, certificate_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (3, 3),
       (4, 3),
       (1, 4),
       (5, 4),
       (6, 5),
       (7, 5),
       (6, 6),
       (8, 6);

INSERT INTO role
(name_role)
VALUES ("Admin"),
       ("User"),
       ("Guest");

INSERT INTO user
(login, password, name_user, surname, id_role)
VALUES ("user1", "user1", "Ivan", "Ivanov", 2),
       ("user2", "user2", "Petr", "Petrov", 2),
       ("user3", "user3","Denis", "Denisov", 2),
       ("admin", "admin","Dmitry", "Dmitriev", 1);

INSERT INTO `orders`
(purchase_date, total, id_user)
VALUES ("2020-11-01T10:00:09", 525.00, 1),
       ("2020-11-02T11:00:10", 290.00, 2),
       ("2020-11-02T12:00:12", 825.00, 3),
       ("2020-11-03T12:00:12", 100.00, 1),
       ("2020-11-03T13:00:15", 80.00, 2),
       ("2020-11-03T13:00:16", 500.00, 3);

INSERT INTO certificate_order
(order_id, certificate_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (3, 6),
       (3, 1),
       (3, 2),
       (4, 3),
       (5, 4),
       (6, 1);






