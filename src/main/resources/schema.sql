CREATE EXTENSION IF NOT EXISTS vector;
DROP EXTENSION pgml;
CREATE EXTENSION IF NOT EXISTS pgml;

CREATE TABLE IF NOT EXISTS summary (
    id serial primary key,
    session_id varchar(255),
    title varchar(255),
    content text,
    embeddings vector
);

CREATE TABLE IF NOT EXISTS full_text (
    id serial primary key,
    session_id varchar(255),
    title varchar(255),
    sequence int,
    content text
)