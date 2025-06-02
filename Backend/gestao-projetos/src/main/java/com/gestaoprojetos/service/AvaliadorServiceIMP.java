package com.gestaoprojetos.service;

import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Avaliacao;
import com.gestaoprojetos.model.Avaliador;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.repository.AvaliadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service para Avaliador, estende a base genérica de CRUD.
 * Inclui métodos extras para associar avaliação e projeto,
 * além de validações e lançamentos de exceções em caso de falhas.
 */
@Service
@Transactional
public class AvaliadorServiceIMP extends
        BasicRepositoryIMP<AvaliadorRepository, Avaliador, Long> {

    public AvaliadorServiceIMP(AvaliadorRepository repository) {
        super(repository);
    }

    /**
     * Cria um novo Avaliador.
     *
     * @param avaliador Dados do avaliador a ser criado (deve ter nome, cpf, email, etc.).
     * @return Avaliador salvo no banco.
     * @throws BadRequestException se algum dado obrigatório estiver ausente.
     */
    public Avaliador criarAvaliador(Avaliador avaliador) {
        validarCamposObrigatorios(avaliador);
//         Garante que não haja CPF duplicado, por exemplo:
        if (getRepository().existsByCpf(avaliador.getCpf())) {
            throw new BadRequestException("CPF já cadastrado: " + avaliador.getCpf());
        }
        return save(avaliador);
    }

    /**
     * Atualiza os dados de um Avaliador existente.
     *
     * @param id         ID do avaliador a ser atualizado.
     * @param dadosNovos Objeto Avaliador contendo os novos dados.
     * @return Avaliador atualizado.
     * @throws ResourceNotFoundException se nenhum Avaliador for encontrado com esse ID.
     * @throws BadRequestException       se campos obrigatórios estiverem ausentes.
     */
    public Avaliador atualizarAvaliador(Long id, Avaliador dadosNovos) {
        // Verifica se existe no banco ou lança ResourceNotFoundException
        Avaliador existente = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Avaliador não encontrado com ID: " + id));

        // Validação básica de campos (nome, cpf, email, telefone)
        validarCamposObrigatorios(dadosNovos);

        // Atualiza apenas os campos que fazem sentido
        existente.setNome(dadosNovos.getNome());
        existente.setCpf(dadosNovos.getCpf());
        existente.setEmail(dadosNovos.getEmail());
        existente.setTelefone(dadosNovos.getTelefone());

        return save(existente);
    }

    /**
     * Busca um Avaliador por ID, ou lança exceção se não existir.
     *
     * @param id ID do avaliador.
     * @return Avaliador encontrado.
     * @throws ResourceNotFoundException se não for encontrado.
     */
    public Avaliador buscarPorId(Long id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Avaliador não encontrado com ID: " + id));
    }

    /**
     * Retorna a lista de todos os Avaliadores cadastrados.
     *
     * @return Lista de Avaliadores.
     * Se não houver nenhum, retorna lista vazia.
     */
    public List<Avaliador> listarTodos() {
        return findAll();
    }

    /**
     * Deleta um Avaliador por ID.
     *
     * @param id ID do avaliador a ser removido.
     * @throws ResourceNotFoundException se o Avaliador não existir.
     */
    public void deletarPorId(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Avaliador não encontrado com ID: " + id);
        }
        deleteById(id);
    }

    /**
     * Atribui uma avaliação (Avaliacao) a este Avaliador.
     *
     * @param idAvaliador ID do Avaliador que fará a avaliação.
     * @param avaliacao   Objeto Avaliacao que será associado.
     * @return Avaliador com a lista de avaliações atualizada.
     * @throws ResourceNotFoundException se o Avaliador não for encontrado.
     * @throws BadRequestException       se o objeto Avaliacao for nulo.
     */
    public Avaliador atribuirAvaliacao(Long idAvaliador, Avaliacao avaliacao) {
        if (avaliacao == null) {
            throw new BadRequestException("Objeto Avaliacao não pode ser nulo.");
        }
        Avaliador avaliador = buscarPorId(idAvaliador);

        // Verifica se o avaliador já possui uma lista de avaliações
        if (avaliador.getAvaliacoes() == null) {
            avaliador.setAvaliacoes(new java.util.ArrayList<>());
        }
        avaliador.getAvaliacoes().add(avaliacao);

        // Se Avaliacao tiver vínculo bidirecional, faça avaliacao.setAvaliador(avaliador);
        avaliacao.setAvaliador(avaliador);

        return save(avaliador);
    }

    /**
     * Atribui um Projeto a este Avaliador (caso seu modelo permita essa associação).
     *
     * @param idAvaliador ID do Avaliador.
     * @param projeto     Objeto Projeto que será associado.
     * @return Avaliador atualizado.
     * @throws ResourceNotFoundException se o Avaliador não existir.
     * @throws BadRequestException       se o Projeto for nulo.
     */
    public Avaliador atribuirProjeto(Long idAvaliador, Projeto projeto) {
        if (projeto == null) {
            throw new BadRequestException("Objeto Projeto não pode ser nulo.");
        }
        Avaliador avaliador = buscarPorId(idAvaliador);

        if (avaliador.getProjetos() == null) {
            avaliador.setProjetos(new java.util.ArrayList<>());
        }
        avaliador.getProjetos().add(projeto);

        return save(avaliador);
    }

    /**
     * Valida campos obrigatórios básicos antes de salvar ou atualizar.
     *
     * @param avaliador Objeto Avaliador a ser validado.
     * @throws BadRequestException se algum campo obrigatório estiver nulo ou vazio.
     */
    private void validarCamposObrigatorios(Avaliador avaliador) {
        if (avaliador.getNome() == null || avaliador.getNome().trim().isEmpty()) {
            throw new BadRequestException("O campo 'nome' é obrigatório.");
        }
        if (avaliador.getCpf() == null || avaliador.getCpf().trim().isEmpty()) {
            throw new BadRequestException("O campo 'cpf' é obrigatório.");
        }
        if (avaliador.getEmail() == null || avaliador.getEmail().trim().isEmpty()) {
            throw new BadRequestException("O campo 'email' é obrigatório.");
        }
    }
}
