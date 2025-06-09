package com.gestaoprojetos.service;

import com.gestaoprojetos.controller.DTO.PessoaDTO;
import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Avaliacao;
import com.gestaoprojetos.model.Avaliador;
import com.gestaoprojetos.repository.AvaliacaoRepository;
import com.gestaoprojetos.repository.AvaliadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliadorServiceIMP(AvaliadorRepository repository, AvaliacaoRepository avaliacaoRepository) {
        super(repository);
        this.avaliacaoRepository = avaliacaoRepository;
    }

    /**
     * Cria um novo Avaliador a partir de um DTO.
     *
     * @param avaliadorReqt Objeto DTO preenchido com os dados do avaliador.
     * @return Avaliador persistido (com ID gerado).
     * @throws BadRequestException se campos obrigatórios estiverem ausentes ou inválidos.
     */
    public Avaliador criarAvaliador(PessoaDTO.PessoaRequestDTO avaliadorReqt) throws BadRequestException {
        if (avaliadorReqt == null) {
            throw new BadRequestException("Objeto Avaliador não pode ser nulo.");
        }
        Avaliador avaliador = new Avaliador(
                null,
                avaliadorReqt.getNome(),
                avaliadorReqt.getCPF(),
                avaliadorReqt.getEmail(),
                avaliadorReqt.getTelefone(),
                null
        );
        validarCamposObrigatorios(avaliador);
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
    public Avaliador atualizarAvaliador(Long id, Avaliador dadosNovos) throws ResourceNotFoundException {
        // Verifica se existe no banco ou lança ResourceNotFoundException
        Avaliador existente = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliador não encontrado com ID: " + id));

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
     * Busca um Avaliador por ID ou lança ResourceNotFoundException.
     *
     * @param id ID do avaliador a ser buscado.
     * @return Avaliador encontrado.
     * @throws ResourceNotFoundException se nenhum Avaliador for encontrado com esse ID.
     */
    public Avaliador buscarPorId(Long id) throws ResourceNotFoundException {
        return findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Avaliador não encontrado com ID: " + id)
                );
    }

    public PessoaDTO.PessoaResponseDTO LazyBuscarPorId(Long id) {
        Avaliador avaliador = buscarPorId(id);
        return new PessoaDTO.PessoaResponseDTO(avaliador.getId(), avaliador.getNome(), avaliador.getTelefone(), avaliador.getEmail());
    }

    /**
     * Retorna a lista de todos os Avaliadores cadastrados.
     *
     * @return Lista de Avaliadores.
     * Se não houver nenhum, retorna lista vazia.
     */
    public List<PessoaDTO.PessoaResponseDTO> listarTodos() {
        return findAll().stream().map(
                avaliador -> new PessoaDTO.PessoaResponseDTO(
                        avaliador.getId(),
                        avaliador.getNome(),
                        avaliador.getTelefone(),
                        avaliador.getEmail()
                )
        ).toList();
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
    public Avaliador atribuirAvaliacao(Long idAvaliador, Long idAvaliacao) {
        Avaliador avaliador = buscarPorId(idAvaliador);

        Avaliacao avaliacao = avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com ID: " + idAvaliacao));

        if (avaliador.getAvaliacoes() == null) {
            avaliador.setAvaliacoes(new ArrayList<>());
        }

        // evita adicionar a mesma avaliação duas vezes
        if (!avaliador.getAvaliacoes().contains(avaliacao)) {
            avaliador.getAvaliacoes().add(avaliacao);
            // Se for bidirecional, setar avaliador na avaliação
            avaliacao.setAvaliador(avaliador);
        }

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
