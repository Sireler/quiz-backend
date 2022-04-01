CREATE TABLE IF NOT EXISTS answers (
    id bigserial primary key,
    question_id bigserial NOT NULL,
    body text NOT NULL,
    is_correct boolean NOT NULL,
    created_at timestamp default now(),
    updated_at timestamp default now()
);
