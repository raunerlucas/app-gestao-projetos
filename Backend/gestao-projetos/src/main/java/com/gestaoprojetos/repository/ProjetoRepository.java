package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Os principais métodos que você já tem disponíveis são:
 * <p>
 * Listar por ID (Read by ID):
 * findById(Long id)  // Retorna Optional<Projeto>
 * getById(Long id)   // Retorna Projeto (em versões mais antigas do Spring)
 * getReferenceById(Long id)  // Retorna uma referência (proxy) do Projeto
 * <p>
 * Listar todos os registros (Read All):
 * findAll()  // Retorna List<Projeto>
 * <p>
 * Outros métodos úteis que já vêm incluídos:
 * save(Projeto projeto) - Salva ou atualiza uma entidade
 * deleteById(Long id) - Exclui pelo ID
 * delete(Projeto projeto) - Exclui uma entidade
 * count() - Retorna o número total de registros
 * existsById(Long id) - Verifica se existe um projeto com o ID informado
 */
@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    List<Projeto> findByDataEnvioBefore(LocalDate dataEnvioBefore);
}
