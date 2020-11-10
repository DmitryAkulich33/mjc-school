USE gift_certificate;

INSERT INTO tag
    (name_tag, lock_tag)
VALUES ("food", 0),
       ("delivery", 0),
       ("jewellery", 0),
       ("beauty", 0),
       ("restaurant", 0),
       ("entertainment", 0),
       ("spa", 0),
       ("tour", 0);

INSERT INTO certificate
(name_certificate, description, price, creation_date, lock_certificate, duration)
VALUES ("Certificate for ordering food delivery", "Buffet set of oriental dishes from an off-site restaurant", 25.00,
        "2020-10-22T10:00:09", 0, 90),
       ("Jewelry purchase certificate",
        "The certificate gives the right to purchase any jewelry for women and men in the 7 CARAT salon for the amount of 500 rubles",
        500.00, "2020-10-22T11:00:09", 0, 365),
       ("Certificate for visiting a beauty salon",
        "Facial cleansing, peeling with application of a mask or a comprehensive Anti-age program in the Beauty Salon and SPA",
        100.00, "2020-10-22T11:30:09", 0, 120),
       ("Certificate for dinner in a restaurant", "Food and drink without check limit at Viet Express", 80.00,
        "2020-10-22T11:45:11", 0, 90),
       ("SPA certificate", "Romantic SPA date for two any day", 110.00, "2020-10-22T11:55:11", 0, 200),
       ("Certificate for a guided tour of your choice",
        "Guided tour of Antalya, Pamukkale, Ephesus, Pergamum and Three for 1 person", 300.00, "2020-10-22T12:00:15", 0,
        365);

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

INSERT INTO user
    (name_user, surname, lock_user)
VALUES ("Ivan", "Ivanov", 0),
       ("Petr", "Petrov", 0),
       ("Denis", "Denisov", 0);

INSERT INTO `order`
    (purchase_date, total, id_user)
VALUES ("2020-11-01T10:00:09", 525.00, 1),
       ("2020-11-02T11:00:10", 290.00, 2),
       ("2020-11-02T12:00:12", 825.00, 3),
       ("2020-11-03T12:00:12", 100.00, 1),
       ("2020-11-03T13:00:10", 80.00,  2);

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
       (5, 4);





