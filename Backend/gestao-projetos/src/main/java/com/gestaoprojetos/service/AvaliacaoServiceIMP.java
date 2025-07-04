package com.gestaoprojetos.service;

import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Avaliacao;
import com.gestaoprojetos.model.Avaliador;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.model.Status;
import com.gestaoprojetos.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Serviço para a entidade Avaliacao.
 * Estende BasicRepositoryIMP para herdar operações de CRUD genéricas.
 * Adiciona validações de negócio e métodos auxiliares específicos de Avaliacao.
 */
@Service
@Transactional
public class AvaliacaoServiceIMP extends BasicRepositoryIMP<
        AvaliacaoRepository, Avaliacao, Long> {

    private final AvaliadorRepository avaliadorRepository;
    private final StatusRepository statusRepository;
    private final ProjetoRepository projetoRepository;

    /**
     * Construtor: injeta AvaliacaoRepository, AvaliadorRepository, StatusRepository e ProjetoRepository.
     * - AvaliacaoRepository é passado para BasicRepositoryIMP.
     */
    public AvaliacaoServiceIMP(
            AvaliacaoRepository avaliacaoRepository,
            AvaliadorRepository avaliadorRepository,
            StatusRepository statusRepository,
            ProjetoRepository projetoRepository
    ) {
        super(avaliacaoRepository);
        this.avaliadorRepository = avaliadorRepository;
        this.statusRepository = statusRepository;
        this.projetoRepository = projetoRepository;
    }

    /**
     * Cria uma nova Avaliacao no banco.
     *
     * @param avaliacao Objeto preenchido (parecer, nota, dataAvaliacao, avaliador.id, status.id, projeto.id).
     * @return Avaliacao salva (com ID gerado).
     * @throws BadRequestException se campos obrigatórios estiverem ausentes ou inválidos.
     * @throws ResourceNotFoundException se Avaliador, Status ou Projeto associados não existirem.
     */
    public Avaliacao criarAvaliacao(Avaliacao avaliacao) {
        validarCamposBasicos(avaliacao);

        // 1. Verificar existência de Avaliador
        Long avaliadorId = avaliacao.getAvaliador().getId();
        if (avaliadorId == null) {
            throw new BadRequestException("O ID do Avaliador deve ser informado.");
        }
        Avaliador avaliador = avaliadorRepository.findById(avaliadorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Avaliador não encontrado com ID: " + avaliadorId)
                );
        avaliacao.setAvaliador(avaliador);

        // 2. Verificar existência de Status
        Long statusId = avaliacao.getStatus().getId();
        if (statusId == null) {
            throw new BadRequestException("O ID do Status deve ser informado.");
        }
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Status não encontrado com ID: " + statusId)
                );
        avaliacao.setStatus(status);

        // 3. Verificar existência de Projeto
        Long projetoId = avaliacao.getProjeto().getId();
        if (projetoId == null) {
            throw new BadRequestException("O ID do Projeto deve ser informado.");
        }
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projeto não encontrado com ID: " + projetoId)
                );
        avaliacao.setProjeto(projeto);

        // 4. Persistir e retornar
        return save(avaliacao);
    }

    /**
     * Atualiza uma Avaliacao existente.
     *
     * @param id         ID da Avaliacao a ser atualizada.
     * @param dadosNovos Objeto com novos dados (parecer, nota, dataAvaliacao, avaliador.id, status.id, projeto.id).
     * @return Avaliacao atualizada.
     * @throws ResourceNotFoundException se Avaliacao, Avaliador, Status ou Projeto não existirem.
     * @throws BadRequestException       se campos obrigatórios estiverem ausentes ou inválidos.
     */
    public Avaliacao atualizarAvaliacao(Long id, Avaliacao dadosNovos) {
        // 1. Buscar Avaliacao existente ou lançar 404
        Avaliacao existente = findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Avaliação não encontrada com ID: " + id)
                );

        // 2. Validar campos
        validarCamposBasicos(dadosNovos);

        // 3. Se quisermos trocar de Avaliador
        Long novoAvaliadorId = dadosNovos.getAvaliador().getId();
        if (novoAvaliadorId == null) {
            throw new BadRequestException("O ID do Avaliador deve ser informado.");
        }
        if (!novoAvaliadorId.equals(existente.getAvaliador().getId())) {
            Avaliador novoAvaliador = avaliadorRepository.findById(novoAvaliadorId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Avaliador não encontrado com ID: " + novoAvaliadorId)
                    );
            existente.setAvaliador(novoAvaliador);
        }

        // 4. Se quisermos trocar de Status
        Long novoStatusId = dadosNovos.getStatus().getId();
        if (novoStatusId == null) {
            throw new BadRequestException("O ID do Status deve ser informado.");
        }
        if (!novoStatusId.equals(existente.getStatus().getId())) {
            Status novoStatus = statusRepository.findById(novoStatusId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Status não encontrado com ID: " + novoStatusId)
                    );
            existente.setStatus(novoStatus);
        }

        // 5. Se quisermos trocar de Projeto
        Long novoProjetoId = dadosNovos.getProjeto().getId();
        if (novoProjetoId == null) {
            throw new BadRequestException("O ID do Projeto deve ser informado.");
        }
        if (!novoProjetoId.equals(existente.getProjeto().getId())) {
            Projeto novoProjeto = projetoRepository.findById(novoProjetoId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Projeto não encontrado com ID: " + novoProjetoId)
                    );
            existente.setProjeto(novoProjeto);
        }

        // 6. Atualizar os demais campos
        existente.setParecer(dadosNovos.getParecer());
        existente.setNota(dadosNovos.getNota());
        existente.setDataAvaliacao(dadosNovos.getDataAvaliacao());

        // 7. Persistir e retornar
        return save(existente);
    }

    /**
     * Busca uma Avaliacao por ID ou lança ResourceNotFoundException.
     *
     * @param id ID da Avaliação.
     * @return Avaliacao encontrada.
     * @throws ResourceNotFoundException se não houver avaliação com esse ID.
     */
    public Avaliacao buscarPorId(Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Avaliação não encontrada com ID: " + id)
                );
    }

    /**
     * Retorna todas as Avaliacoes cadastradas.
     *
     * @return lista de Avaliacao (pode vir vazia).
     */
    public List<Avaliacao> listarTodos() {
        return findAll();
    }

    /**
     * Deleta uma Avaliacao por ID.
     *
     * @param id ID da Avaliação a ser removida.
     * @throws ResourceNotFoundException se não existir avaliação com esse ID.
     */
    public void deletarPorId(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Avaliação não encontrada com ID: " + id);
        }
        deleteById(id);
    }

    /**
     * Lista todas as avaliações feitas em uma data específica.
     *
     * @param data LocalDate da avaliação.
     * @return lista de Avaliacao nesse dia.
     */
    public List<Avaliacao> listarPorData(LocalDate data) {
        return getRepository().findByDataAvaliacao(data);
    }

    /**
     * Lista todas as avaliações de um determinado projeto.
     *
     * @param projetoId ID do Projeto.
     * @return lista de Avaliacao do projeto.
     * @throws ResourceNotFoundException se o projeto não existir.
     */
    public List<Avaliacao> listarPorProjeto(Long projetoId) {
        projetoRepository.findById(projetoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projeto não encontrado com ID: " + projetoId)
                );
        return getRepository().findByProjetoId(projetoId);
    }

    /**
     * Lista todas as avaliações realizadas por um determinado avaliador.
     *
     * @param avaliadorId ID do Avaliador.
     * @return lista de Avaliacao desse avaliador.
     * @throws ResourceNotFoundException se o avaliador não existir.
     */
    public List<Avaliacao> listarPorAvaliador(Long avaliadorId) {
        avaliadorRepository.findById(avaliadorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Avaliador não encontrado com ID: " + avaliadorId)
                );
        return getRepository().findByAvaliadorId(avaliadorId);
    }

    /**
     * Valida campos obrigatórios básicos da Avaliacao antes de criar ou atualizar:
     * - parecer não pode ser nulo/vazio
     * - nota não pode ser nula e deve estar entre 0.0 e 10.0
     * - dataAvaliacao não pode ser nula e não pode ser futuro
     * - avaliador, status e projeto não podem ser nulos (apenas ID já é checado depois)
     *
     * @param avaliacao Objeto a ser validado.
     * @throws BadRequestException se algum campo estiver inválido.
     */
    private void validarCamposBasicos(Avaliacao avaliacao) {
        if (avaliacao.getParecer() == null || avaliacao.getParecer().trim().isEmpty()) {
            throw new BadRequestException("O campo 'parecer' é obrigatório e não pode ficar vazio.");
        }
        if (avaliacao.getNota() == null) {
            throw new BadRequestException("O campo 'nota' é obrigatório.");
        }
        if (avaliacao.getNota() < 0.0 || avaliacao.getNota() > 10.0) {
            throw new BadRequestException("A nota deve estar entre 0.0 e 10.0.");
        }
        if (avaliacao.getDataAvaliacao() == null) {
            throw new BadRequestException("O campo 'dataAvaliacao' é obrigatório.");
        }
        if (avaliacao.getDataAvaliacao().isAfter(LocalDate.now())) {
            throw new BadRequestException("A data de avaliação não pode ser futura: "
                    + avaliacao.getDataAvaliacao());
        }
        if (avaliacao.getAvaliador() == null || avaliacao.getAvaliador().getId() == null) {
            throw new BadRequestException("O campo 'avaliador.id' é obrigatório.");
        }
        if (avaliacao.getStatus() == null || avaliacao.getStatus().getId() == null) {
            throw new BadRequestException("O campo 'status.id' é obrigatório.");
        }
        if (avaliacao.getProjeto() == null || avaliacao.getProjeto().getId() == null) {
            throw new BadRequestException("O campo 'projeto.id' é obrigatório.");
        }
    }
}