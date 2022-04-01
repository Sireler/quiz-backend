CREATE TABLE IF NOT EXISTS questions (
    id bigserial primary key,
    topic_id bigserial NOT NULL,
    body text NOT NULL,
    created_at timestamp default now(),
    updated_at timestamp default now()
);
