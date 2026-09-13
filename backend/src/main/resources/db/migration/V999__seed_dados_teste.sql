INSERT INTO cargos (codigo_do_cargo, descricao_do_cargo)
VALUES
    ('SuporteN1', 'Tecnico Suporte N1'),
    ('SuporteN2', 'Tecnico Suporte N2'),
    ('Implantação', 'Tecnico Sucesso do cliente'),
    ('Líder', 'Lider de Projetos');

INSERT INTO departamentos (codigo_do_departamento, descricao_do_departamento)
VALUES
    ('Rh', 'Departamento Pessoal'),
    ('SdC', 'Sucesso do Cliente'),
    ('Dev', 'Desenvolvedor');

INSERT INTO funcionarios (nome, cpf)
VALUES
    ('Andrey R.V', '12345678901'),
    ('Agustin', '98765432109');

INSERT INTO vinculos (empresa, matricula, cargo_id, departamento_id, funcionario_id)
VALUES
    ('Dixi Soluções', '001', 1, 1, 1),
    ('Dixi Soluções', '002', 3, 1, 2);
