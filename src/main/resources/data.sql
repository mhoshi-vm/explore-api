INSERT INTO summary (session_id, title, content) VALUES ('A1', 'Session1', 'AAAA');
INSERT INTO summary (session_id, title, content) VALUES ('B1', 'Session2', 'BBBB');
INSERT INTO summary (session_id, title, content) VALUES ('C1', 'Session3', 'CCCC');

UPDATE summary SET embeddings = pgml.embed('intfloat/e5-small', content)::vector;

INSERT INTO full_text (session_id, sequence, content) VALUES ('A1', 1, 'AAAAA');
INSERT INTO full_text (session_id, sequence, content) VALUES ('A1', 2, 'BBBBB');
INSERT INTO full_text (session_id, sequence, content) VALUES ('A1', 3, 'CCCCC');

INSERT INTO full_text (session_id, sequence, content) VALUES ('B1', 1, 'AAAAA');
INSERT INTO full_text (session_id, sequence, content) VALUES ('B1', 2, 'BBBBB');
INSERT INTO full_text (session_id, sequence, content) VALUES ('B1', 3, 'CCCCC');

INSERT INTO full_text (session_id, sequence, content) VALUES ('C1', 1, 'AAAAA');
INSERT INTO full_text (session_id, sequence, content) VALUES ('C1', 2, 'BBBBB');
INSERT INTO full_text (session_id, sequence, content) VALUES ('C1', 3, 'CCCCC');

