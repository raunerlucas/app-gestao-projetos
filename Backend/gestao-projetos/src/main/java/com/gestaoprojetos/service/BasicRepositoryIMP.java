package com.gestaoprojetos.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * BasicRepositoryImpl é a classe genérica que encapsula
 * operações CRUD básicas para qualquer repositório JPA.
 *
 * @param <T>  tipo de repositório (que deve estender JpaRepository)
 * @param <E>  tipo da entidade (por exemplo, Cliente, Produto, etc.)
 * @param <ID> tipo do identificador da entidade (por exemplo, Long, Integer, String)
 */
abstract class BasicRepositoryIMP<
        T extends JpaRepository<E, ID>,
        E,
        ID
        > {

    /**
     * Aqui guardamos a referência ao repositório concreto (injetado pelo Spring).
     */
    private final T repository;

    /**
     * Construtor fará com que o Spring injete o bean de T automaticamente.
     * Ao criar uma subclasse, você deve chamar super(meuRepositórioConcreto).
     */
    protected BasicRepositoryIMP(T repository) {
        this.repository = repository;
    }

    /**
     * Retorna o repositório injetado, caso você precise usá‐lo diretamente
     * em algum metodo mais específico.
     */
    protected T getRepository() {
        return repository;
    }

    /**
     * Salva (ou atualiza) uma entidade.
     */
    public E save(E entity) {
        return repository.save(entity);
    }

    /**
     * Busca uma entidade pelo ID (retorna Optional<E>).
     */
    public Optional<E> findById(ID id) {
        return repository.findById(id);
    }

    /**
     * Retorna todos os registros da entidade em forma de List<E>.
     */
    public List<E> findAll() {
        return repository.findAll();
    }

    /**
     * Deleta por ID.
     */
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    /**
     * Deleta a entidade passada como parâmetro.
     */
    public void delete(E entity) {
        repository.delete(entity);
    }

    /**
     * Verifica se existe uma entidade com o ID fornecido.
     */
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
    }
}
