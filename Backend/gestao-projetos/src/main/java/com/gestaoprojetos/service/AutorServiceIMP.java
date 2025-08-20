package com.gestaoprojetos.service;

import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.repository.AutorRepository;
import com.gestaoprojetos.repository.BasicRepositoryIMP;
import com.gestaoprojetos.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço para a entidade Autor.
 * Estende BasicRepositoryIMP para herdar operações genéricas de CRUD.
 * Adiciona validações de dados pessoais e métodos para gerenciar associação com Projetos.
 */
@Service
@Transactional
public class AutorServiceIMP extends BasicRepositoryIMP<AutorRepository, Autor, Long> {

    private final ProjetoRepository projetoRepository;

    /**
     * Construtor: o Spring injeta AutorRepository e ProjetoRepository.
     * - AutorRepository é passado para BasicRepositoryIMP.
     * - ProjetoRepository é usado para validar existência de Projetos.
     */
    public AutorServiceIMP(AutorRepository autorRepository, ProjetoRepository projetoRepository) {
        super(autorRepository);
        this.projetoRepository = projetoRepository;
    }

    /**
     * Cria um novo Autor.
     *
     * @param autor Objeto preenchido (nome, cpf, email, telefone).
     * @return Autor persistido (com ID gerado).
     * @throws BadRequestException se campos obrigatórios estiverem ausentes ou inválidos.
     */
    public Autor criarAutor(Autor autor) throws BadRequestException {
        if (autor == null) {
            throw new BadRequestException("Objeto Autor não pode ser nulo.");
        }

        validarCamposBasicos(autor);
        return save(autor);
    }

    /**
     * Atualiza dados de um Autor existente.
     *
     * @param id         ID do Autor a ser atualizado.
     * @param dadosNovos Objeto com novos dados (nome, cpf, email, telefone).
     * @return Autor atualizado.
     * @throws ResourceNotFoundException se não houver Autor com esse ID.
     * @throws BadRequestException       se campos obrigatórios estiverem ausentes ou inválidos.
     */
    public Autor atualizarAutor(Long id, Autor dadosNovos) throws ResourceNotFoundException {
        Autor existente = findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Autor não encontrado com ID: " + id)
                );

        validarCamposBasicos(dadosNovos);

        existente.setNome(dadosNovos.getNome());
        existente.setCpf(dadosNovos.getCpf());
        existente.setEmail(dadosNovos.getEmail());
        existente.setTelefone(dadosNovos.getTelefone());

        return save(existente);
    }

    /**
     * Busca um Autor por ID ou lança ResourceNotFoundException.
     *
     * @param id ID do Autor.
     * @return Autor encontrado.
     * @throws ResourceNotFoundException se não houver Autor com esse ID.
     */
    public Autor buscarPorId(Long id) throws ResourceNotFoundException {
        return findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Autor não encontrado com ID: " + id)
                );
    }

    /**
     * Retorna todos os Autores cadastrados.
     *
     * @return lista de Autor (pode vir vazia).
     */
    public List<Autor> listarTodos() {
        return findAll();
    }

    /**
     * Deleta um Autor por ID.
     *
     * @param id ID do Autor a ser removido.
     * @throws ResourceNotFoundException se não houver Autor com esse ID.
     */
    public void deletarPorId(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Autor não encontrado com ID: " + id);
        }
        deleteById(id);
    }

    /**
     * Adiciona um Projeto ao Autor.
     *
     * @param autorId ID do Autor.
     * @param projeto Objeto Projeto (deve ter ID existente).
     * @return Autor atualizado.
     * @throws ResourceNotFoundException se Autor ou Projeto não existirem.
     * @throws BadRequestException       se o objeto Projeto for nulo ou já estiver associado.
     */
    public Autor adicionarProjeto(Long autorId, Projeto projeto) throws ResourceNotFoundException, BadRequestException {
        if (projeto == null || projeto.getId() == null) {
            throw new BadRequestException("Projeto não pode ser nulo e deve ter um ID válido.");
        }

        Autor autor = buscarPorId(autorId);

        // Verificar se o projeto existe
        Projeto projetoExistente = projetoRepository.findById(projeto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projeto não encontrado com ID: " + projeto.getId())
                );

        // Verificar se o projeto já está associado ao autor
        if (autor.getProjetos().stream().anyMatch(p -> p.getId().equals(projeto.getId()))) {
            throw new BadRequestException("Projeto já está associado a este autor.");
        }

        autor.getProjetos().add(projetoExistente);
        return save(autor);
    }

    /**
     * Remove um Projeto do Autor.
     *
     * @param autorId   ID do Autor.
     * @param projetoId ID do Projeto a ser removido.
     * @return Autor atualizado.
     * @throws ResourceNotFoundException se Autor não existir ou se Projeto não estiver associado ao Autor.
     */
    public Autor removerProjeto(Long autorId, Long projetoId) throws ResourceNotFoundException {
        Autor autor = buscarPorId(autorId);

        Projeto projetoEncontrado = autor.getProjetos().stream()
                .filter(p -> p.getId().equals(projetoId))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Projeto não encontrado com ID: " + projetoId + " para o Autor ID: " + autorId
                        )
                );

        autor.getProjetos().remove(projetoEncontrado);
        return save(autor);
    }

    /**
     * Lista os Projetos aos quais esse Autor está associado.
     *
     * @param autorId ID do Autor.
     * @return lista de Projeto associados.
     * @throws ResourceNotFoundException se Autor não existir.
     */
    public List<Projeto> listarProjetos(Long autorId) {
        Autor autor = buscarPorId(autorId);
        return autor.getProjetos();
    }

    /**
     * Valida campos obrigatórios básicos do Autor antes de criar ou atualizar:
     * - nome não pode ser nulo/vazio
     * - cpf não pode ser nulo/vazio
     * - email não pode ser nulo/vazio
     * - telefone não pode ser nulo/vazio
     *
     * @param autor Objeto a ser validado.
     * @throws BadRequestException se algum campo estiver inválido.
     */
    private void validarCamposBasicos(Autor autor) throws BadRequestException {
        if (autor.getNome() == null || autor.getNome().trim().isEmpty()) {
            throw new BadRequestException("O campo 'nome' é obrigatório e não pode ser vazio.");
        }
        if (autor.getCpf() == null || autor.getCpf().trim().isEmpty()) {
            throw new BadRequestException("O campo 'cpf' é obrigatório e não pode ser vazio.");
        }
        if (autor.getEmail() == null || autor.getEmail().trim().isEmpty()) {
            throw new BadRequestException("O campo 'email' é obrigatório e não pode ser vazio.");
        }
        if (autor.getTelefone() == null || autor.getTelefone().trim().isEmpty()) {
            throw new BadRequestException("O campo 'telefone' é obrigatório e não pode ser vazio.");
        }
    }
}