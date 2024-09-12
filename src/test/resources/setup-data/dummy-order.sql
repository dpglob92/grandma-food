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

-- 5421030d-04db-46ab-b5f2-2429c6b2d0d9
INSERT INTO product
	(id, available, description, food_category, name, price, created_at, updated_at)
VALUES(
	X'5421030d04db46abb5f22429c6b2d0d9',
	1,
	'lorem ipsum',
	'CHICKEN',
	'Chicken curry',
	'120.23',
	NOW(),
	NOW()
);

-- cbc1892f-fc74-4791-bd62-86d8952abc7c
INSERT INTO restaurant_order
	(id, additional_information, client_id, created_at, updated_at)
VALUES(
	X'cbc1892ffc744791bd6286d8952abc7c',
	'lorem ipsum',
	'3f06af63-a93c-11e4-9797-00505690773f',
	NOW(),
	NOW()
);
