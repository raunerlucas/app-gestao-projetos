package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Avaliador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliadorRepository extends JpaRepository<Avaliador, Long> {
}
