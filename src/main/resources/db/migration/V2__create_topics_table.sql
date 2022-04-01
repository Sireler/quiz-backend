CREATE TABLE IF NOT EXISTS topics (
    id bigserial primary key,
    user_id bigserial NOT NULL,
    name varchar(255) NOT NULL,
    created_at timestamp default now(),
    updated_at timestamp default now()
);
