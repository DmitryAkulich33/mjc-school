INSERT INTO tag VALUES (1, 'food', 0),
                       (2, 'delivery', 0);
INSERT INTO certificate VALUES (1, 'Certificate for one purchase', 'Certificate for one going to the shop', 50.0, '2020-10-22T11:45:11', null, 0, 365),
                               (2, 'Certificate for dinner in a restaurant', 'Food and drink without check limit at Viet Express', 100.0, '2020-11-22T12:45:11', null, 0, 100);
INSERT INTO tag_certificate VALUES (1, 1),
                                   (1, 2),
                                   (2, 1);
INSERT INTO user VALUES (1, 'Ivan', 'Ivanov', 0),
                        (2, 'Petr', 'Petrov', 0);
INSERT INTO orders VALUES (1, '2020-11-22T12:45:11', 150, 0, 1),
                          (2, '2020-11-22T12:45:11', 50, 0, 2);
INSERT INTO certificate_order VALUES (1, 1),
                                     (1, 2),
                                     (2, 1);