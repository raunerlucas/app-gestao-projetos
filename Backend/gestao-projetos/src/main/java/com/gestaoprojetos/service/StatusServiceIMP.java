package com.gestaoprojetos.service;


import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Status;
import com.gestaoprojetos.repository.BasicRepositoryIMP;
import com.gestaoprojetos.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Serviço para a entidade Status.
 * Estende BasicRepositoryIMP para herdar operações genéricas de CRUD.
 * Adiciona validações específicas para Status (por ex., garantir que o ID não esteja em uso).
 */
@Service
@Transactional
public class StatusServiceIMP
        extends BasicRepositoryIMP<StatusRepository, Status, Long> {

    /**
     * Construtor: injetamos StatusRepository e passamos ao super().
     */
    public StatusServiceIMP(StatusRepository repository) {
        super(repository);
    }

    /**
     * Cria um novo Status no banco.
     * Como não há @GeneratedValue, é obrigatório que dados.id não seja nulo e nem já esteja em uso.
     *
     * @param status Objeto Status contendo id e description.
     * @return Status persistido.
     * @throws BadRequestException se id for nulo, description for nulo/vazio, ou id já existir.
     */
    public Status criarStatus(Status status) {
        validarCamposBasicos(status);

        Long id = status.getId();
        if (id == null) {
            throw new BadRequestException("O campo 'id' é obrigatório para criar um Status.");
        }
        if (existsById(id)) {
            throw new BadRequestException("Já existe um Status com ID: " + id);
        }

        return save(status);
    }

    /**
     * Atualiza um Status existente.
     * Só é possível mudar a description; o id permanece o mesmo.
     *
     * @param id         ID do Status a ser atualizado.
     * @param dadosNovos Objeto Status com nova description (id pode ser ignorado ou igual ao path).
     * @return Status atualizado.
     * @throws ResourceNotFoundException se não houver Status com esse ID.
     * @throws BadRequestException       se novos dados inválidos (description nulo/vazio).
     */
    public Status atualizarStatus(Long id, Status dadosNovos) {
        Status existente = findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Status não encontrado com ID: " + id)
                );

        if (dadosNovos.getDescription() == null || dadosNovos.getDescription().trim().isEmpty()) {
            throw new BadRequestException("O campo 'description' é obrigatório e não pode ficar vazio.");
        }

        existente.setDescription(dadosNovos.getDescription());
        return save(existente);
    }

    /**
     * Busca um Status por ID ou lança ResourceNotFoundException.
     *
     * @param id ID do Status.
     * @return Status encontrado.
     * @throws ResourceNotFoundException se não existir.
     */
    public Status buscarPorId(Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Status não encontrado com ID: " + id)
                );
    }

    /**
     * Retorna todos os status cadastrados.
     *
     * @return lista de Status (pode vir vazia).
     */
    public List<Status> listarTodos() {
        return findAll();
    }

    /**
     * Deleta um Status por ID.
     *
     * @param id ID do Status a ser removido.
     * @throws ResourceNotFoundException se não existir.
     */
    public void deletarPorId(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Status não encontrado com ID: " + id);
        }
        deleteById(id);
    }

    /**
     * Retorna todos os Status predefinidos (Enum Values) como objetos.
     * Útil para popular drop-downs ou interface que exiba opções fixas.
     *
     * @return lista de Status vindo do enum Values.
     */
    public List<Status> listarPredefinidos() {
        return
                java.util.Arrays.stream(Status.Values.values())
                        .map(Status.Values::toStatus)
                        .collect(Collectors.toList());
    }

    /**
     * Valida campos obrigatórios de Status:
     * - id pode ser validado apenas em criarStatus (já checado lá).
     * - description não pode ser nulo ou vazio.
     *
     * @param status Objeto a ser validado.
     * @throws BadRequestException se description inválida.
     */
    private void validarCamposBasicos(Status status) {
        if (status.getDescription() == null || status.getDescription().trim().isEmpty()) {
            throw new BadRequestException("O campo 'description' é obrigatório e não pode ficar vazio.");
        }
    }
}
