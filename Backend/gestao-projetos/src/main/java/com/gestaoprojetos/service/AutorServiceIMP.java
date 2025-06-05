package com.gestaoprojetos.service;

import com.gestaoprojetos.controller.DTO.AutorLazyDTO;
import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.repository.AutorRepository;
import com.gestaoprojetos.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço para a entidade Autor.
 * Estende BasicRepositoryIMP para herdar operações genéricas de CRUD.
 * Adiciona validações de dados pessoais (nome, cpf, email, telefone) e métodos
 * para gerenciar associação com Projetos.
 */
@Service
@Transactional
public class AutorServiceIMP extends BasicRepositoryIMP<
        AutorRepository, Autor, Long> {

    private final ProjetoRepository projetoRepository;

    /**
     * Construtor: o Spring injeta AutorRepository e ProjetoRepository.
     * - AutorRepository é passado para BasicRepositoryIMP.
     * - ProjetoRepository é usado para validar existência de Projetos.
     */
    public AutorServiceIMP(
            AutorRepository autorRepository,
            ProjetoRepository projetoRepository
    ) {
        super(autorRepository);
        this.projetoRepository = projetoRepository;
    }

    /**
     * Cria um novo Autor.
     *
     * @param autor Objeto preenchido (nome, cpf, email, telefone).
     * @return Autor persistido (com ID gerado).
     * @throws BadRequestException se campos obrigatórios (nome, cpf, email, telefone) estiverem ausentes ou inválidos.
     */
    public Autor criarAutor(Autor autor) {
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
    public Autor atualizarAutor(Long id, Autor dadosNovos) {
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
    public Autor buscarPorId(Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Autor não encontrado com ID: " + id)
                );
    }

    public AutorLazyDTO LazyBuscarPorId(Long id) {
        Autor autor = buscarPorId(id);
        return new AutorLazyDTO(autor.getId(), autor.getNome(), autor.getTelefone(), autor.getEmail());
    }

    /**
     * Retorna todos os Autores cadastrados.
     *
     * @return lista de Autor (pode vir vazia).
     */
    public List<AutorLazyDTO> listarTodos() {
        return findAll().stream().map(
                autor -> new AutorLazyDTO(
                        autor.getId(),
                        autor.getNome(),
                        autor.getTelefone(),
                        autor.getEmail()
                )
        ).toList();
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
     * Lista os Projetos aos quais esse Autor está associado.
     *
     * @param autorId ID do Autor.
     * @return lista de Projeto associados.
     * @throws ResourceNotFoundException se Autor não existir.
     */
    public List<Projeto> listarProjetos(Long autorId) {
        Autor autor = buscarPorId(autorId);
        // getProjetos já foi inicializado no construtor da entidade
        return autor.getProjetos();
    }

    /**
     * Adiciona um Projeto ao Autor.
     *
     * @param autorId ID do Autor.
     * @param projeto Objeto Projeto que deve conter, pelo menos, o ID existente.
     * @return Autor atualizado (com a lista de projetos modificada).
     * @throws ResourceNotFoundException se Autor ou Projeto não existirem.
     * @throws BadRequestException       se objeto Projeto for nulo ou se ID estiver ausente.
     */
    public Autor adicionarProjeto(Long autorId, Projeto projeto) {
        if (projeto == null) {
            throw new BadRequestException("Objeto Projeto não pode ser nulo.");
        }
        Long projetoId = projeto.getId();
        if (projetoId == null) {
            throw new BadRequestException("O ID do Projeto deve ser informado.");
        }

        Autor autor = buscarPorId(autorId);
        Projeto projetoGerenciado = projetoRepository.findById(projetoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projeto não encontrado com ID: " + projetoId)
                );

        // Evita duplicação: só adiciona se ainda não estiver na lista
        if (autor.getProjetos().stream().noneMatch(p -> p.getId().equals(projetoId))) {
            autor.getProjetos().add(projetoGerenciado);
            // Também atualiza o lado “muitos” do Projeto (bidirecional)
            if (projetoGerenciado.getAutores().stream().noneMatch(a -> a.getId().equals(autorId))) {
                projetoGerenciado.getAutores().add(autor);
            }
            // Salva o Autor (o relacionamento ManyToMany será atualizado)
            return save(autor);
        }

        return autor; // já estava associado
    }

    /**
     * Remove um Projeto da lista de um Autor.
     *
     * @param autorId   ID do Autor.
     * @param projetoId ID do Projeto a ser removido.
     * @return Autor atualizado (sem o Projeto removido).
     * @throws ResourceNotFoundException se Autor ou Projeto não existirem,
     *                                   ou se o Projeto não pertencer ao Autor.
     */
    public Autor removerProjeto(Long autorId, Long projetoId) {
        Autor autor = buscarPorId(autorId);

        Projeto encontrado = autor.getProjetos().stream()
                .filter(p -> p.getId().equals(projetoId))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Projeto não encontrado com ID: " + projetoId
                                        + " para o Autor ID: " + autorId
                        )
                );

        autor.getProjetos().remove(encontrado);
        // Atualiza bidirecional: remove Autor de projeto.getAutores()
        encontrado.getAutores().removeIf(a -> a.getId().equals(autorId));

        return save(autor);
    }

    /**
     * Valida campos básicos herdados de Pessoa:
     * - nome: não pode ser nulo ou vazio
     * - cpf: não pode ser nulo ou vazio
     * - email: não pode ser nulo ou vazio
     * - telefone: não pode ser nulo ou vazio
     *
     * @param autor Objeto Autor a ser validado.
     * @throws BadRequestException se algum campo obrigatório estiver ausente ou vazio.
     */
    private void validarCamposBasicos(Autor autor) {
        if (autor.getNome() == null || autor.getNome().trim().isEmpty()) {
            throw new BadRequestException("O campo 'nome' é obrigatório e não pode ficar vazio.");
        }
        if (autor.getCpf() == null || autor.getCpf().trim().isEmpty()) {
            throw new BadRequestException("O campo 'cpf' é obrigatório e não pode ficar vazio.");
        }
        if (autor.getEmail() == null || autor.getEmail().trim().isEmpty()) {
            throw new BadRequestException("O campo 'email' é obrigatório e não pode ficar vazio.");
        }
        if (autor.getTelefone() == null || autor.getTelefone().trim().isEmpty()) {
            throw new BadRequestException("O campo 'telefone' é obrigatório e não pode ficar vazio.");
        }
        // Aqui você pode adicionar regex para formato de CPF, e-mail, etc., se quiser.
    }
}