-- INSERT INTO tag
-- (name_tag)
-- VALUES
-- ("food"),
-- ("delivery"),
-- ("jewellery"),
-- ("beauty"),
-- ("restaurant"),
-- ("entertainment"),
-- ("spa"),
-- ("tour");
INSERT INTO tag VALUES (1, 'food', 0),
                       (2, 'delivery', 0);
INSERT INTO certificate VALUES (1, 'Certificate for one purchase', 'Certificate for one going to the shop', 50.0, '2020-10-22T11:45:11', null, 0, 365),
                               (2, 'Certificate for dinner in a restaurant', 'Food and drink without check limit at Viet Express', 100.0, '2020-11-22T12:45:11', null, 0, 100);
INSERT INTO tag_certificate VALUES (1, 1),
                                   (1, 2),
                                   (2, 1);