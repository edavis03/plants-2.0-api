CREATE TABLE genus
(
    id         UUID,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE plant ADD genus_id UUID;
ALTER TABLE plant ADD CONSTRAINT fk_genus_id FOREIGN KEY (genus_id) REFERENCES genus(id);