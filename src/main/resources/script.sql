INSERT INTO city(id, name) VALUES (1, 'Tel Aviv');
INSERT INTO city(id, name) VALUES (2, 'Ramat Gan');

insert into image(id,name) values (1,'static/category_default.jpg');
insert into image(id,name) values (2,'static/red_adidas_product.jpg');

INSERT INTO category(id, name, image_id) VALUES (1, 'Men', 1);
INSERT INTO category(id, name, image_id) VALUES (2, 'Women', 1);
INSERT INTO category(id, name, image_id) VALUES (3, 'Boys', 1);
INSERT INTO category(id, name, image_id) VALUES (4, 'Girls', 1);
INSERT INTO category(id, name, image_id) VALUES (5, 'Babies', 1);

insert into product(id,name,description,price,image_id) values (1,'red adidas 2000','greatest men snikers ever',107,2);
INSERT INTO category_product(category_id, product_id) VALUES (1, 1);

