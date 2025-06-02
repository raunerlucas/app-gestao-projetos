package com.gestaoprojetos.service;

import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Avaliacao;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.repository.AutorRepository;
import com.gestaoprojetos.repository.AvaliacaoRepository;
import com.gestaoprojetos.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Serviço para a entidade Projeto.
 * Estende BasicRepositoryIMP para herdar operações genéricas de CRUD.
 * Adiciona validações e métodos para gerenciar Avaliacoes e Autores.
 */
@Service
@Transactional
public class ProjetoServiceIMP extends
        BasicRepositoryIMP<
                ProjetoRepository, Projeto, Long> {

    private final AvaliacaoRepository avaliacaoRepository;
    private final AutorRepository autorRepository;

    /**
     * Construtor: injeta ProjetoRepository, AvaliacaoRepository e AutorRepository.
     * - ProjetoRepository é passado ao super() para herdar CRUD genérico.
     * - Os repositórios auxiliares servem para validações de existência.
     */
    public ProjetoServiceIMP(
            ProjetoRepository projetoRepository,
            AvaliacaoRepository avaliacaoRepository,
            AutorRepository autorRepository
    ) {
        super(projetoRepository);
        this.avaliacaoRepository = avaliacaoRepository;
        this.autorRepository = autorRepository;
    }

    /**
     * Cria um novo Projeto.
     *
     * @param projeto Objeto preenchido (titulo, resumo, dataEnvio, areaTematica).
     * @return Projeto salvo (com ID gerado).
     * @throws BadRequestException se campos obrigatórios estiverem ausentes ou dataEnvio for futura.
     */
    public Projeto criarProjeto(Projeto projeto) {
        validarCamposBasicos(projeto);
        validarDataEnvio(projeto.getDataEnvio());
        // Listas (avaliacoes e autores) já vêm inicializadas no construtor da entidade
        return save(projeto);
    }

    /**
     * Atualiza um Projeto existente.
     *
     * @param id         ID do Projeto a ser atualizado.
     * @param dadosNovos Objeto com novos dados (titulo, resumo, dataEnvio, areaTematica).
     * @return Projeto atualizado.
     * @throws ResourceNotFoundException se não existir projeto com esse ID.
     * @throws BadRequestException       se campos obrigatórios estiverem inválidos.
     */
    public Projeto atualizarProjeto(Long id, Projeto dadosNovos) {
        Projeto existente = findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projeto não encontrado com ID: " + id)
                );

        validarCamposBasicos(dadosNovos);
        validarDataEnvio(dadosNovos.getDataEnvio());

        existente.setTitulo(dadosNovos.getTitulo());
        existente.setResumo(dadosNovos.getResumo());
        existente.setDataEnvio(dadosNovos.getDataEnvio());
        existente.setAreaTematica(dadosNovos.getAreaTematica());
        // Note: não alteramos lista de avaliacoes/autores aqui; use métodos específicos abaixo

        return save(existente);
    }

    /**
     * Busca um Projeto por ID ou lança ResourceNotFoundException.
     *
     * @param id ID do Projeto.
     * @return Projeto encontrado.
     * @throws ResourceNotFoundException se não existir projeto com esse ID.
     */
    public Projeto buscarPorId(Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projeto não encontrado com ID: " + id)
                );
    }

    /**
     * Retorna todos os Projetos cadastrados.
     *
     * @return lista de Projeto (pode vir vazia).
     */
    public List<Projeto> listarTodos() {
        return findAll();
    }

    /**
     * Deleta um Projeto por ID.
     *
     * @param id ID do Projeto a ser removido.
     * @throws ResourceNotFoundException se não existir projeto com esse ID.
     */
    public void deletarPorId(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Projeto não encontrado com ID: " + id);
        }
        deleteById(id);
    }

    /**
     * Adiciona uma Avaliacao a este Projeto.
     *
     * @param projetoId ID do Projeto que receberá a Avaliacao.
     * @param avaliacao Objeto Avaliacao (com parecer, nota, dataAvaliacao).
     * @return Projeto atualizado (com a Avaliacao adicionada).
     * @throws ResourceNotFoundException se o Projeto ou a Avaliacao não existirem.
     * @throws BadRequestException       se o objeto Avaliacao for nulo.
     */
    public Projeto adicionarAvaliacao(Long projetoId, Avaliacao avaliacao) {
        if (avaliacao == null) {
            throw new BadRequestException("Objeto Avaliacao não pode ser nulo.");
        }

        Projeto projeto = buscarPorId(projetoId);

        // Primeiro, persiste a Avaliacao para garantir que ela tenha ID próprio
        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        // Inicializa a lista se estiver vazia (entidade já faz isso, mas reforçamos)
        if (projeto.getAvaliacoes() == null) {
            projeto.setAvaliacoes(new java.util.ArrayList<>());
        }

        // Associa bidirecionalmente: Avaliacao.projeto = projeto
        avaliacaoSalva.setProjeto(projeto);
        projeto.getAvaliacoes().add(avaliacaoSalva);

        return save(projeto);
    }

    /**
     * Remove uma Avaliacao do Projeto (por ID da Avaliacao).
     *
     * @param projetoId   ID do Projeto.
     * @param avaliacaoId ID da Avaliacao a ser removida.
     * @return Projeto atualizado (sem a Avaliacao removida).
     * @throws ResourceNotFoundException se Projeto ou Avaliacao não existirem, ou Avaliacao não pertencer ao Projeto.
     */
    public Projeto removerAvaliacao(Long projetoId, Long avaliacaoId) {
        Projeto projeto = buscarPorId(projetoId);

        Avaliacao encontrada = projeto.getAvaliacoes().stream()
                .filter(a -> a.getId().equals(avaliacaoId))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Avaliação não encontrada com ID: " + avaliacaoId
                                        + " para o Projeto ID: " + projetoId
                        )
                );

        projeto.getAvaliacoes().remove(encontrada);
        // Como orphanRemoval=true e Cascade.ALL, JPA apagará a linha de Avaliacao no banco
        return save(projeto);
    }

    /**
     * Adiciona um Autor a este Projeto.
     *
     * @param projetoId ID do Projeto.
     * @param autor     Objeto Autor (deve ter ID existente ou novos dados).
     * @return Projeto atualizado (com o Autor adicionado).
     * @throws ResourceNotFoundException se Projeto não existir ou se autor.id não existir.
     * @throws BadRequestException       se o objeto Autor for nulo.
     */
    public Projeto adicionarAutor(Long projetoId, Autor autor) {
        if (autor == null) {
            throw new BadRequestException("Objeto Autor não pode ser nulo.");
        }

        Projeto projeto = buscarPorId(projetoId);

        // Se o autor já tem ID, verifica existência; caso contrário, salva novo Autor
        Autor autorGerenciado;
        if (autor.getId() != null) {
            autorGerenciado = autorRepository.findById(autor.getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Autor não encontrado com ID: " + autor.getId())
                    );
        } else {
            autorGerenciado = autorRepository.save(autor);
        }

        // Inicializa a lista se estiver vazia
        if (projeto.getAutores() == null) {
            projeto.setAutores(new java.util.ArrayList<>());
        }

        // Evita duplicar: se o autor já estiver na lista, não adiciona
        if (projeto.getAutores().stream().noneMatch(a -> a.getId().equals(autorGerenciado.getId()))) {
            projeto.getAutores().add(autorGerenciado);
        }

        return save(projeto);
    }

    /**
     * Remove um Autor do Projeto (por ID do Autor).
     *
     * @param projetoId ID do Projeto.
     * @param autorId   ID do Autor a ser removido.
     * @return Projeto atualizado (sem o Autor removido).
     * @throws ResourceNotFoundException se Projeto ou Autor não existirem, ou se Autor não pertencer ao Projeto.
     */
    public Projeto removerAutor(Long projetoId, Long autorId) {
        Projeto projeto = buscarPorId(projetoId);

        Autor encontrado = projeto.getAutores().stream()
                .filter(a -> a.getId().equals(autorId))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Autor não encontrado com ID: " + autorId
                                        + " para o Projeto ID: " + projetoId
                        )
                );

        projeto.getAutores().remove(encontrado);
        // Em uma relação ManyToMany sem orphanRemoval, apenas atualizamos a tabela intermediária
        return save(projeto);
    }

    /**
     * Valida campos obrigatórios básicos do Projeto antes de criar ou atualizar:
     * - titulo não pode ser nulo/vazio
     * - resumo não pode ser nulo/vazio
     * - dataEnvio não pode ser nula e não pode ser futura
     * - areaTematica não pode ser nulo/vazio
     *
     * @param projeto Objeto a ser validado.
     * @throws BadRequestException se algum campo estiver inválido.
     */
    private void validarCamposBasicos(Projeto projeto) {
        if (projeto.getTitulo() == null || projeto.getTitulo().trim().isEmpty()) {
            throw new BadRequestException("O campo 'titulo' é obrigatório e não pode ser vazio.");
        }
        if (projeto.getResumo() == null || projeto.getResumo().trim().isEmpty()) {
            throw new BadRequestException("O campo 'resumo' é obrigatório e não pode ser vazio.");
        }
        if (projeto.getDataEnvio() == null) {
            throw new BadRequestException("O campo 'dataEnvio' é obrigatório.");
        }
        if (projeto.getAreaTematica() == null || projeto.getAreaTematica().trim().isEmpty()) {
            throw new BadRequestException("O campo 'areaTematica' é obrigatório e não pode ser vazio.");
        }
    }

    /**
     * Verifica se a data de envio não está no futuro.
     *
     * @param dataEnvio LocalDate informado.
     * @throws BadRequestException se dataEnvio for posterior a hoje.
     */
    private void validarDataEnvio(LocalDate dataEnvio) {
        if (dataEnvio.isAfter(LocalDate.now())) {
            throw new BadRequestException("A data de envio não pode ser futura: " + dataEnvio);
        }
    }
}


