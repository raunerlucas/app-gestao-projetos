package com.gestaoprojetos.service;

import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Cronograma;
import com.gestaoprojetos.model.Premio;
import com.gestaoprojetos.repository.BasicRepositoryIMP;
import com.gestaoprojetos.repository.CronogramaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para a entidade Cronograma.
 * Estende BasicRepositoryIMP para herdar operações CRUD genéricas.
 * Adiciona validações de negócio (datas) e métodos para gerenciar a lista de Prêmios.
 */
@Service
@Transactional
public class CronogramaServiceIMP extends
        BasicRepositoryIMP<CronogramaRepository, Cronograma, Long> {


    /**
     * Construtor fará com que o Spring injete o bean de T automaticamente.
     * Ao criar uma subclasse, você deve chamar super(meuRepositórioConcreto).
     *
     * @param repository
     */
    protected CronogramaServiceIMP(CronogramaRepository repository) {
        super(repository);
    }

    /**
     * Cria um novo Cronograma no banco.
     *
     * @param cronograma Objeto contendo dataInicio, dataFim, descricao e (opcionalmente) status.
     * @return Cronograma salvo (com ID gerado).
     * @throws BadRequestException se houver campos ausentes ou dataFim < dataInicio.
     */
    public Cronograma criarCronograma(Cronograma cronograma) {
        validarCamposBasicos(cronograma);
        validarIntervaloDatas(cronograma.getDataInicio(), cronograma.getDataFim());
        // Status já sai default “NAO_INICIADO”, mas se vier nulo:
        if (cronograma.getStatus() == null) {
            cronograma.setStatus(Cronograma.StatusCronograma.NAO_INICIADO);
        }
        return save(cronograma);
    }

    /**
     * Atualiza um Cronograma existente no banco.
     *
     * @param id         ID do Cronograma a ser atualizado.
     * @param dadosNovos Objeto contendo novos dados (dataInicio, dataFim, descricao, status).
     * @return Cronograma atualizado.
     * @throws ResourceNotFoundException se não existir Cronograma com esse ID.
     * @throws BadRequestException       se campos obrigatórios estiverem ausentes ou datas inválidas.
     */
    public Cronograma atualizarCronograma(Long id, Cronograma dadosNovos) {
        Cronograma existente = findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cronograma não encontrado com ID: " + id)
                );

        validarCamposBasicos(dadosNovos);
        validarIntervaloDatas(dadosNovos.getDataInicio(), dadosNovos.getDataFim());

        // Atualiza apenas os campos permitidos:
        existente.setDataInicio(dadosNovos.getDataInicio());
        existente.setDataFim(dadosNovos.getDataFim());
        existente.setDescricao(dadosNovos.getDescricao());
        existente.setStatus(dadosNovos.getStatus());

        return save(existente);
    }

    /**
     * Busca um Cronograma por ID ou lança ResourceNotFoundException.
     *
     * @param id ID do Cronograma.
     * @return Cronograma encontrado.
     * @throws ResourceNotFoundException se não houver nenhum com esse ID.
     */
    public Cronograma buscarPorId(Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cronograma não encontrado com ID: " + id)
                );
    }

    /**
     * Retorna todos os Cronogramas cadastrados.
     *
     * @return lista de Cronograma (pode vir vazia).
     */
    public List<Cronograma> listarTodos() {
        return findAll();
    }

    /**
     * Deleta um Cronograma por ID.
     *
     * @param id ID do Cronograma a ser removido.
     * @throws ResourceNotFoundException se não existir Cronograma com esse ID.
     */
    public void deletarPorId(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cronograma não encontrado com ID: " + id);
        }
        deleteById(id);
    }

    /**
     * Adiciona um novo Prêmio ao Cronograma.
     *
     * @param idCronograma ID do Cronograma ao qual o Prêmio será associado.
     * @param premio       Objeto Premio já preenchido (nome, descricao, anoEdicao, etc.).
     *                     Deve ter a propriedade `cronograma` setada para este objeto Cronograma.
     * @return Cronograma atualizado (incluindo o novo Prêmio na lista).
     * @throws ResourceNotFoundException se o Cronograma não existir.
     * @throws BadRequestException       se o objeto Premio for nulo ou não tiver campos obrigatórios.
     */
    public Cronograma adicionarPremio(Long idCronograma, Premio premio) {
        if (premio == null) {
            throw new BadRequestException("Objeto Premio não pode ser nulo.");
        }

        Cronograma cronograma = buscarPorId(idCronograma);

        // Se o Prêmio ainda não tiver cronograma associado, fazemos:
        premio.setCronograma(cronograma);

        // Inicializa a lista, caso seja null (normalmente já vem como ArrayList vazio)
        if (cronograma.getPremios() == null) {
            cronograma.setPremios(new java.util.ArrayList<>());
        }

        cronograma.getPremios().add(premio);
        // Como CascadeType.ALL está configurado, basta salvar Cronograma
        return save(cronograma);
    }

    /**
     * Remove um Prêmio do Cronograma (por ID do Prêmio).
     *
     * @param idCronograma ID do Cronograma.
     * @param idPremio     ID do Prêmio a ser removido da lista.
     * @return Cronograma atualizado (sem o Prêmio removido).
     * @throws ResourceNotFoundException se Cronograma ou Prêmio não existirem.
     */
    public Cronograma removerPremio(Long idCronograma, Long idPremio) {
        Cronograma cronograma = buscarPorId(idCronograma);

        // Tentar localizar o prêmio na lista
        Premio premioEncontrado = cronograma.getPremios().stream()
                .filter(premio -> premio.getId().equals(idPremio))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Prêmio não encontrado com ID: " + idPremio
                                + " para o cronograma " + idCronograma)
                );

        // Remove da lista; orphanRemoval = true apagará o registro de Prêmio no banco
        cronograma.getPremios().remove(premioEncontrado);
        return save(cronograma);
    }

    /**
     * Valida os campos obrigatórios básicos da entidade Cronograma:
     * - dataInicio não pode ser nula
     * - dataFim não pode ser nula
     * - descricao não pode ser nula ou vazia
     *
     * @param cronograma objeto a ser validado
     * @throws BadRequestException se algum campo estiver inválido
     */
    private void validarCamposBasicos(Cronograma cronograma) {
        if (cronograma.getDataInicio() == null) {
            throw new BadRequestException("A data de início é obrigatória.");
        }
        if (cronograma.getDataFim() == null) {
            throw new BadRequestException("A data de fim é obrigatória.");
        }
        if (cronograma.getDescricao() == null || cronograma.getDescricao().trim().isEmpty()) {
            throw new BadRequestException("A descrição do cronograma é obrigatória.");
        }
        // Poderíamos também verificar se descricao tem tamanho máximo, etc.
    }

    /**
     * Valida se a dataFim é posterior ou igual a dataInicio.
     *
     * @param inicio data de início do cronograma
     * @param fim    data de fim do cronograma
     * @throws BadRequestException se dataFim for antes de dataInicio
     */
    private void validarIntervaloDatas(java.time.LocalDate inicio, java.time.LocalDate fim) {
        if (fim.isBefore(inicio)) {
            throw new BadRequestException(
                    "A data de fim (" + fim + ") não pode ser anterior à data de início (" + inicio + ")."
            );
        }
    }
}