package com.gestaoprojetos.service;

import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Cronograma;
import com.gestaoprojetos.model.Premio;
import com.gestaoprojetos.repository.BasicRepositoryIMP;
import com.gestaoprojetos.repository.CronogramaRepository;
import com.gestaoprojetos.repository.PremioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço para a entidade Premio.
 * Estende BasicRepositoryIMP para herdar operações genéricas de CRUD.
 * Adiciona validações de campos e métodos específicos, como buscar por cronograma.
 */
@Service
@Transactional
public class PremioServiceIMP extends
        BasicRepositoryIMP<PremioRepository, Premio, Long> {

    private final CronogramaRepository cronogramaRepository;

    /**
     * Construtor: injeta PremioRepository e CronogramaRepository.
     * - PremioRepository é passado para BasicRepositoryIMP (para herdar CRUD genérico).
     * - CronogramaRepository é usado para validar existência do cronograma.
     */
    public PremioServiceIMP(PremioRepository premioRepository,
                            CronogramaRepository cronogramaRepository) {
        super(premioRepository);
        this.cronogramaRepository = cronogramaRepository;
    }

    /**
     * Cria um novo Premio no banco.
     *
     * @param premio Objeto preenchido (nome, descricao, anoEdicao, cronograma.id).
     * @return Premio salvo (com ID gerado).
     * @throws BadRequestException       se algum campo obrigatório estiver ausente ou inválido.
     * @throws ResourceNotFoundException se o cronograma associado não existir.
     */
    public Premio criarPremio(Premio premio) {
        // 1. Validar campos obrigatórios básicos
        validarCamposBasicos(premio);

        // 2. Verificar se o cronograma informado existe no banco
        Long cronogramaId = premio.getCronograma().getId();
        if (cronogramaId == null) {
            throw new BadRequestException("O ID do cronograma deve ser informado.");
        }
        Cronograma cronograma = cronogramaRepository.findById(cronogramaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cronograma não encontrado com ID: " + cronogramaId
                        )
                );
        // Associar o objeto Cronograma gerenciado pela JPA ao prêmio
        premio.setCronograma(cronograma);

        // 3. Persistir e retornar
        return save(premio);
    }

    /**
     * Atualiza um Premio existente.
     *
     * @param id         ID do Premio a ser atualizado.
     * @param dadosNovos Objeto com os novos dados (nome, descricao, anoEdicao, cronograma.id).
     * @return Premio atualizado.
     * @throws ResourceNotFoundException se o prêmio ou o cronograma associado não existirem.
     * @throws BadRequestException       se campos obrigatórios estiverem ausentes ou inválidos.
     */
    public Premio atualizarPremio(Long id, Premio dadosNovos) {
        // 1. Busca o prêmio original
        Premio existente = findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Prêmio não encontrado com ID: " + id)
                );

        // 2. Valida os campos de dadosNovos
        validarCamposBasicos(dadosNovos);

        // 3. Verifica se deve trocar o cronograma
        Long novoCronogramaId = dadosNovos.getCronograma().getId();
        if (novoCronogramaId == null) {
            throw new BadRequestException("O ID do cronograma deve ser informado.");
        }
        // Se o cronograma mudou, validamos existência
        if (!novoCronogramaId.equals(existente.getCronograma().getId())) {
            Cronograma novoCronograma = cronogramaRepository.findById(novoCronogramaId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Cronograma não encontrado com ID: " + novoCronogramaId
                            )
                    );
            existente.setCronograma(novoCronograma);
        }

        // 4. Atualiza os demais campos
        existente.setNome(dadosNovos.getNome());
        existente.setDescricao(dadosNovos.getDescricao());
        existente.setAnoEdicao(dadosNovos.getAnoEdicao());

        // 5. Persiste e retorna
        return save(existente);
    }

    /**
     * Busca um Prêmio por ID, ou lança ResourceNotFoundException.
     *
     * @param id ID do Prêmio.
     * @return Premio encontrado.
     * @throws ResourceNotFoundException se não existir prêmio com esse ID.
     */
    public Premio buscarPorId(Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Prêmio não encontrado com ID: " + id)
                );
    }

    /**
     * Retorna todos os Prêmios cadastrados.
     *
     * @return lista de Premio (pode vir vazia).
     */
    public List<Premio> listarTodos() {
        return findAll();
    }

    /**
     * Deleta um Prêmio por ID.
     *
     * @param id ID do Prêmio a ser removido.
     * @throws ResourceNotFoundException se não existir prêmio com esse ID.
     */
    public void deletarPorId(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Prêmio não encontrado com ID: " + id);
        }
        deleteById(id);
    }

    /**
     * Retorna todos os Prêmios vinculados a um Cronograma específico.
     *
     * @param cronogramaId ID do Cronograma.
     * @return lista de Premio desse Cronograma (pode ser vazia).
     * @throws ResourceNotFoundException se o Cronograma não existir.
     */
    public List<Premio> listarPorCronograma(Long cronogramaId) {
        // Garante que o cronograma existe (pode lançar ResourceNotFoundException)
        cronogramaRepository.findById(cronogramaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cronograma não encontrado com ID: " + cronogramaId)
                );
        // Busca via method name criado em PremioRepository
        return getRepository().findByCronogramaId(cronogramaId);
    }

    /**
     * Valida campos obrigatórios básicos do Premio antes de criar ou atualizar:
     * - nome não pode ser nulo/vazio
     * - descricao não pode ser nulo/vazio
     * - anoEdicao não pode ser nulo e deve estar entre 0 e 9999
     * - cronograma não pode ser nulo
     *
     * @param premio Objeto a ser validado.
     * @throws BadRequestException se algum campo estiver inválido.
     */
    private void validarCamposBasicos(Premio premio) {
        if (premio.getNome() == null || premio.getNome().trim().isEmpty()) {
            throw new BadRequestException("O campo 'nome' é obrigatório e não pode ficar vazio.");
        }
        if (premio.getDescricao() == null || premio.getDescricao().trim().isEmpty()) {
            throw new BadRequestException("O campo 'descricao' é obrigatório e não pode ficar vazio.");
        }
        if (premio.getAnoEdicao() == null) {
            throw new BadRequestException("O campo 'anoEdicao' é obrigatório.");
        }
        // Verifica se tem até 4 dígitos e não é negativo
        if (premio.getAnoEdicao() < 0 || premio.getAnoEdicao() > 9999) {
            throw new BadRequestException("Ano da edição deve estar entre 0 e 9999.");
        }
        if (premio.getCronograma() == null || premio.getCronograma().getId() == null) {
            throw new BadRequestException("O campo 'cronograma.id' é obrigatório.");
        }
    }
}