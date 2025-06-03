package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * ProjetoRepository é a interface que define os métodos de acesso aos dados dos projetos.
 * Ela estende JpaRepository para fornecer operações CRUD básicas e consultas personalizadas.
 */
@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    @Query("SELECT p FROM _projeto p LEFT JOIN FETCH p.autores WHERE p.avaliacoes IS EMPTY")
    List<Projeto> findProjetosSemAvaliacao();

    @Query("SELECT p FROM _projeto p JOIN p.avaliacoes a ORDER BY a.nota DESC")
    List<Projeto> findProjetosVencedoresOrderByNotaDesc();
}
