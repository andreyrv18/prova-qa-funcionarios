ALTER TABLE funcionarios
    ADD COLUMN deleted_at TIMESTAMP NULL;

ALTER TABLE vinculos
    ADD COLUMN deleted_at TIMESTAMP NULL;
