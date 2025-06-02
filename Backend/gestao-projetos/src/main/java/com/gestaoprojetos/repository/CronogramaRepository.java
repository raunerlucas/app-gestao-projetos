package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Cronograma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {

    /**
     * Busca um Cronograma baseado no ID de um Premio que pertence a ele.
     *
     * Por causa do relacionamento:
     *   Cronograma → List<Premio> premios
     * cada Premio tem um campo 'id' e um campo 'cronograma'.
     *
     * A consulta derivada deve usar 'premios_Id':
     *   <nomeDoAtributoDaLista>_<propriedadeDoElementoNaLista>
     */
    Optional<Cronograma> findByPremios_Id(Long premioId);
}
