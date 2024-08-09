CREATE TABLE IF NOT EXISTS categories (
    id VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL,
    merchant_category_codes INTEGER[],
    CONSTRAINT categories_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS accounts (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    description VARCHAR(50) NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT accounts_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category_balances (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    category_id VARCHAR(20) NOT NULL,
    account_id uuid NOT NULL,
    balance decimal NOT NULL,
    CONSTRAINT category_balances_pkey PRIMARY KEY (id),
    CONSTRAINT category_balances_categories_fkey FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT category_balances_accounts_fkey FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT category_balances_uniq UNIQUE (category_id, account_id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    category_balance_id uuid NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    amount DECIMAL NOT NULL,
    merchant VARCHAR(255) NOT NULL,
    mcc INTEGER NOT NULL,
    CONSTRAINT transactions_pkey PRIMARY KEY (id),
    CONSTRAINT transactions_category_balances_fkey FOREIGN KEY (category_balance_id) REFERENCES category_balances(id)
);