CREATE TABLE IF NOT EXISTS summary (
    id serial primary key,
    session_id varchar(255),
    context text
);

CREATE TABLE IF NOT EXISTS full_text (
    id serial primary key,
    session_id varchar(255),
    sequence int,
    context text
)