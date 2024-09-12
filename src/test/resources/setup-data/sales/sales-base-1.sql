-- 3f06af63-a93c-11e4-9797-00505690773f
INSERT INTO client
    (id, delivery_address, document_id, document_type, email, name, phone, created_at, updated_at)
VALUES (
    X'3f06af63a93c11e4979700505690773f',
    'Street 10 # 10',
    '100300200',
    'CC',
    'juan@example.com',
    'Juan',
    '310-2223344',
     NOW(),
     NOW()
);

-- ------------------------ Products -------------------------------------

-- Product 1 02184924-1d2a-4006-bbb2-31fe08ad46c1
INSERT INTO product
	(id, available, description, food_category, name, price, created_at, updated_at)
VALUES(
	X'021849241d2a4006bbb231fe08ad46c1',
	1,
	'Delicious chocolate cake',
	'DESSERTS',
	'Chocolate Cake',
	'25.50',
	NOW(),
	NOW()
);

-- Product 2 9c53676f-d4dd-4cca-b69f-8203cf70d474
INSERT INTO product
	(id, available, description, food_category, name, price, created_at, updated_at)
VALUES(
	X'9c53676fd4dd4ccab69f8203cf70d474',
	1,
	'Freshly caught salmon',
	'FISH',
	'Wild Salmon',
	'18.75',
	NOW(),
	NOW()
);

-- Product 3 5421030d-04db-46ab-b5f2-2429c6b2d0d9
INSERT INTO product
	(id, available, description, food_category, name, price, created_at, updated_at)
VALUES(
	X'5421030d04db46abb5f22429c6b2d0d9',
	1,
	'Delicious beef burger with cheese',
	'HAMBURGERS_AND_HOTDOGS',
	'Cheeseburger',
	'8.60',
	NOW(),
	NOW()
);

-- ------------------------ Orders -------------------------------------

-- Order 1 cbc1892f-fc74-4791-bd62-86d8952abc7c
INSERT INTO restaurant_order
	(id, additional_information, client_id, created_at, updated_at)
VALUES(
	X'cbc1892ffc744791bd6286d8952abc7c',
	'lorem ipsum',
	X'3f06af63a93c11e4979700505690773f',
	'2022-02-14 14:00:00',
	'2022-02-14 14:00:00'
);

-- Order 2 7396a04e-b7a3-44b0-af4e-5eea2ea0ad9d
INSERT INTO restaurant_order
	(id, additional_information, client_id, created_at, updated_at)
VALUES(
	X'7396a04eb7a344b0af4e5eea2ea0ad9d',
	'lorem ipsum',
	X'3f06af63a93c11e4979700505690773f',
	'2022-10-12 5:00:00',
    '2022-10-12 5:00:00'
);

-- ------------------------ Orders Items -------------------------------------

-- For order 1

INSERT INTO restaurant_order_item
	(order_id, product_id, quantity)
VALUES(
	X'cbc1892ffc744791bd6286d8952abc7c',
	X'021849241d2a4006bbb231fe08ad46c1',
	2
);

INSERT INTO restaurant_order_item
	(order_id, product_id, quantity)
VALUES(
	X'cbc1892ffc744791bd6286d8952abc7c',
	X'9c53676fd4dd4ccab69f8203cf70d474',
	4
);

-- For order 2

INSERT INTO restaurant_order_item
	(order_id, product_id, quantity)
VALUES(
	X'7396a04eb7a344b0af4e5eea2ea0ad9d',
	X'021849241d2a4006bbb231fe08ad46c1',
	1
);

INSERT INTO restaurant_order_item
	(order_id, product_id, quantity)
VALUES(
	X'7396a04eb7a344b0af4e5eea2ea0ad9d',
	X'5421030d04db46abb5f22429c6b2d0d9',
	8
);