package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Cronograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {
    Optional<Cronograma> findByPremioId(Long premioId);
}
