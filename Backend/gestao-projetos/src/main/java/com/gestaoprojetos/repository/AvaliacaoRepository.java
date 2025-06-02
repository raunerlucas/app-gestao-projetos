package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    /**
     * Exemplo: busca todas as avaliações feitas em uma data específica.
     */
    List<Avaliacao> findByDataAvaliacao(LocalDate dataAvaliacao);

    /**
     * Exemplo: busca todas as avaliações de um projeto específico.
     * O Spring Data JPA inferirá que o campo de Avaliacao é 'projeto.id'.
     */
    List<Avaliacao> findByProjetoId(Long projetoId);

    /**
     * Exemplo: busca todas as avaliações de um avaliador específico.
     */
    List<Avaliacao> findByAvaliadorId(Long avaliadorId);
}
