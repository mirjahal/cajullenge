-- Accounts
INSERT INTO
    accounts (id,description,created_at)
VALUES
    ('1c589934-d43e-4f60-a4af-067c6feb7394','primary account','2024-08-07 11:22:45.102');

-- Category Balances
INSERT INTO
    category_balances (id,category_id,account_id,balance)
VALUES
	 ('cc060c2e-9473-434f-b23b-d1eca62f6e4f','CASH','1c589934-d43e-4f60-a4af-067c6feb7394',3000),
	 ('28d3bb74-1c47-4028-9ae6-015ac29f873e','FOOD','1c589934-d43e-4f60-a4af-067c6feb7394',400),
	 ('f5a50ca8-1006-4a7a-9c5a-b26062aa45c8','MEAL','1c589934-d43e-4f60-a4af-067c6feb7394',400);
