INSERT INTO city(id, name) VALUES (1, 'Tel Aviv');
INSERT INTO city(id, name) VALUES (2, 'Ramat Gan');

insert into image(id,name) values (1,'static/category_default.jpg');
insert into image(id,name) values (2,'static/product1.jpg');
insert into image(id,name) values (3,'static/product2.jpg');
insert into image(id,name) values (4,'static/product3.jpg');
insert into image(id,name) values (5,'static/product4.jpg');
insert into image(id,name) values (6,'static/product5.jpg');
insert into image(id,name) values (7,'static/product6.jpg');
insert into image(id,name) values (8,'static/product7.jpg');
insert into image(id,name) values (9,'static/product8.jpg');

INSERT INTO category(id, name, image_id) VALUES (1, 'Men', 1);
INSERT INTO category(id, name, image_id) VALUES (2, 'Women', 1);
INSERT INTO category(id, name, image_id) VALUES (3, 'Boys', 1);
INSERT INTO category(id, name, image_id) VALUES (4, 'Girls', 1);
INSERT INTO category(id, name, image_id) VALUES (5, 'Babies', 1);

insert into product(id,brand,name,description,price,image_id) values (1,'Adidas',  'Black Run 1.0','greatest men snikers ever',107,2);
insert into product(id,brand,name,description,price,image_id) values (2,'AllStars','Star MX','greatest men snikers ever',108,3);
insert into product(id,brand,name,description,price,image_id) values (3,'Adidas',  'White run 2.0','greatest men snikers ever',123,4);
insert into product(id,brand,name,description,price,image_id) values (4,'AllStars','Red cloud','greatest men snikers ever',87,5);
insert into product(id,brand,name,description,price,image_id) values (5,'Adidas',  'Run 1.0','greatest men snikers ever',99,6);
insert into product(id,brand,name,description,price,image_id) values (6,'Nimrod',  'Boys will be boys','greatest men snikers ever',210,7);
insert into product(id,brand,name,description,price,image_id) values (7,'Adidas',  'run 1.0','greatest men snikers ever',102,8);
insert into product(id,brand,name,description,price,image_id) values (8,'Nimrod',  'first steps','great shoes for your baby',75,9);

INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (1, 'BLACK', 99, '_9', 1);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (2, 'BLACK', 99, '_10', 1);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (3, 'BROWN', 99, '_10', 1);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (4, 'RED', 99, '_10', 1);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (5, 'BLACK', 99, '_9', 2);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (6, 'BLACK', 99, '_9', 3);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (7, 'BLACK', 99, '_9', 4);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (8, 'BLACK', 99, '_9', 5);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (9, 'BLACK', 99, '_9', 6);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (10, 'BLACK', 99, '_9', 7);
INSERT INTO stockitem(id, color, quantity, size, product_id) VALUES (11, 'BLACK', 99, '_9', 8);

INSERT INTO category_product(category_id, product_id) VALUES (1, 1);
INSERT INTO category_product(category_id, product_id) VALUES (2, 2);
INSERT INTO category_product(category_id, product_id) VALUES (3, 3);
INSERT INTO category_product(category_id, product_id) VALUES (4, 4);
INSERT INTO category_product(category_id, product_id) VALUES (5, 5);
INSERT INTO category_product(category_id, product_id) VALUES (3, 6);
INSERT INTO category_product(category_id, product_id) VALUES (1, 7);
INSERT INTO category_product(category_id, product_id) VALUES (3, 8);
