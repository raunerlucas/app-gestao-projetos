-- STATUS: pré-cadastra os Estados possíveis de Avaliação
INSERT INTO _status (id, description) VALUES (1, 'Pendente');
INSERT INTO _status (id, description) VALUES (2, 'Avaliado');
INSERT INTO _status (id, description) VALUES (3, 'Em Avaliação');
INSERT INTO _status (id, description) VALUES (4, 'Cancelado');


-- PESSOAS: Cadastros base para Autor e Avaliador
-- Observação: com InheritanceType.TABLE_PER_CLASS, cada entidade concreta (Autor, Avaliador) tem sua própria tabela.
-- Ainda assim, para que o relacionamento Usuario→Pessoa funcione, criamos registros genéricos em _pessoa.

-- INSERT INTO _pessoa (id, nome, cpf, telefone, email) VALUES (1, 'João Silva', '123.456.789-10', '99999-0000', 'joao@example.com');
-- INSERT INTO _pessoa (id, nome, cpf, telefone, email) VALUES (2, 'Maria Souza', '987.654.321-00', '88888-1111', 'maria@example.com');
-- INSERT INTO _pessoa (id, nome, cpf, telefone, email) VALUES (3, 'Carlos Pereira', '111.222.333-44', '77777-2222', 'carlos@example.com');


-- Autor: herda Pessoa (id=1 → João Silva)
INSERT INTO _autor (id, nome, cpf, telefone, email) VALUES (1, 'João Silva', '123.456.789-10', '99999-0000', 'joao@example.com');

-- Avaliador: herda Pessoa (id=2 → Maria Souza)
INSERT INTO _avaliador (id, nome, cpf, telefone, email) VALUES (2, 'Maria Souza', '987.654.321-00', '88888-1111', 'maria@example.com');

-- Outro Autor (id=3 → Carlos Pereira)
INSERT INTO _autor (id, nome, cpf, telefone, email) VALUES (3, 'Carlos Pereira', '111.222.333-44', '77777-2222', 'carlos@example.com');

INSERT INTO _autor (id, nome, cpf, telefone, email) VALUES (4, 'Rauner Lucas', '888.777.666-55', '77777-2222', 'rauner@example.com');


-- CRONOGRAMA: um ciclo de submissão para projetos
-- INSERT INTO _cronograma (id, data_inicio, data_fim, descricao, status) VALUES (1, '2025-06-01', '2025-06-30', 'Cronograma de Junho 2025', 'NAO_INICIADO');


-- PREMIO: vinculado ao cronograma acima
-- INSERT INTO _premio (id, nome, descricao, ano_edicao, cronograma_id) VALUES (1, 'Prêmio Inovação 2025', 'Premiação de melhores projetos', 2025, 1);


-- PROJETO: submetido por Autor (João Silva)
INSERT INTO _projeto (id, titulo, resumo, data_envio, area_tematica) VALUES (1, 'Projeto Exemplo A', 'Resumo de exemplo A', '2025-06-02', 'Tecnologia');
INSERT INTO _projeto (id, titulo, resumo, data_envio, area_tematica) VALUES (2, 'Projeto Exemplo B', 'Resumo de exemplo B', '2025-06-05', 'Educação');


-- Associação ManyToMany entre Projeto e Autor (tabela _projeto_autor)
INSERT INTO _projeto_autor (projeto_id, autor_id) VALUES (1, 1); -- João Silva em Projeto 1
INSERT INTO _projeto_autor (projeto_id, autor_id) VALUES (2, 3); -- Carlos Pereira em Projeto 2


-- AVALIACAO: vínculo entre Avaliador, Projeto e Status
-- Maria Souza (id=2) avalia Projeto 1 com status Pendente
INSERT INTO _avaliacao (id, parecer, nota, data_avaliacao, avaliador_id, status_id, projeto_id) VALUES (1, 'Análise inicial: pontos fortes.', 8.0, '2025-06-10', 2, 1, 1);

-- Maria Souza (id=2) também avalia Projeto 2 com status Em Avaliação
INSERT INTO _avaliacao (id, parecer, nota, data_avaliacao, avaliador_id, status_id, projeto_id) VALUES (2, 'Em análise detalhada.', 0.0, '2025-06-11', 2, 3, 2);


-- USUARIOS: para autenticação (hash da senha “senha123” gerado via BCrypt)
-- João Silva (Pessoa id=1) ganha login “joao”
INSERT INTO _usuario (id, username, password, pessoa_id) VALUES (1, 'joao', '$2a$10$txSNiCqhNZ1XRnXCo4igMuB2Jzi/YBkkLv2ZAH2CHqAyQtbiaV6gW', 1);

-- Maria Souza (Pessoa id=2) ganha login “maria”
INSERT INTO _usuario (id, username, password, pessoa_id) VALUES (2, 'maria', '$2a$10$txSNiCqhNZ1XRnXCo4igMuB2Jzi/YBkkLv2ZAH2CHqAyQtbiaV6gW', 2);

-- Carlos Pereira (Pessoa id=3) ganha login “carlos”
INSERT INTO _usuario (id, username, password, pessoa_id) VALUES (3, 'carlos', '$2a$10$txSNiCqhNZ1XRnXCo4igMuB2Jzi/YBkkLv2ZAH2CHqAyQtbiaV6gW', 3);

-- SELECT MAX(id) FROM _pessoa;

-- Suponha que o maior id seja 3, ajuste a sequence para começar do 4
ALTER SEQUENCE pessoa_seq RESTART WITH 5;
-- Após inserir dados em _usuario, _projeto, etc.
ALTER TABLE _usuario ALTER COLUMN id RESTART WITH 4;
ALTER TABLE _projeto ALTER COLUMN id RESTART WITH 3;
ALTER TABLE _avaliacao ALTER COLUMN id RESTART WITH 3;
ALTER TABLE _status ALTER COLUMN id RESTART WITH 5;
-- Repita para outras tabelas conforme necessário