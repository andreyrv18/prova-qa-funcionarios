CREATE TABLE funcionarios
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf  VARCHAR(11)  NOT NULL UNIQUE
);


CREATE TABLE cargos
(
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    codigo_do_cargo    INT          NOT NULL UNIQUE,
    descricao_do_cargo VARCHAR(255) NOT NULL
);

CREATE TABLE departamentos
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    codigo_departamento    INT          NOT NULL UNIQUE,
    descricao_departamento VARCHAR(255) NOT NULL
);

CREATE TABLE vinculo
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    empresa         VARCHAR(255) NOT NULL,
    matricula       VARCHAR(50)  NOT NULL,
    cargo_id        INT          NOT NULL,
    departamento_id INT          NOT NULL,
    funcionario_id  INT          NOT NULL,
    CONSTRAINT fk_vinculo_cargo FOREIGN KEY (cargo_id) REFERENCES cargos (id),
    CONSTRAINT fk_vinculo_departamento FOREIGN KEY (departamento_id) REFERENCES departamentos (id),
    CONSTRAINT fk_vinculo_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios (id)

);
