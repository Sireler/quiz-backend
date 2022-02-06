CREATE TABLE IF NOT EXISTS users (
    id bigserial primary key,
    username varchar(32) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    email varchar(255) NOT NULL UNIQUE,
    created_at timestamp default now(),
    updated_at timestamp default now()
);