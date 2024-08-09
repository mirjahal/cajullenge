--- Categories
INSERT INTO
    categories (id,active,merchant_category_codes)
VALUES
    ('FOOD',true,'{5411,5412}'),
	('MEAL',true,'{5811,5812}'),
	('CASH',true,NULL);

-- Accounts
INSERT INTO
    accounts (id,description,created_at)
VALUES
    ('031d9e7e-2999-4a93-82b4-bd5d8052d0d8','primary account','2024-08-07 09:27:59.101');

-- Category Balances
INSERT INTO
    category_balances (id,category_id,account_id,balance)
VALUES
	 ('ba83ac14-921f-475a-b87b-285b93b4ffcd','FOOD','031d9e7e-2999-4a93-82b4-bd5d8052d0d8',500),
	 ('0f80dce5-734b-4551-9644-b991423bbfe2','MEAL','031d9e7e-2999-4a93-82b4-bd5d8052d0d8',500),
	 ('5c4ce7e6-802e-4d7d-8640-18efca986a72','CASH','031d9e7e-2999-4a93-82b4-bd5d8052d0d8',500);
