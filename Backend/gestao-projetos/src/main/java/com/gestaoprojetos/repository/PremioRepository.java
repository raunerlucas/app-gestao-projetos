package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Premio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PremioRepository extends JpaRepository<Premio, Long> {
    Optional<Premio> findByNome(String nome);

    List<Premio> findByCronogramaId(Long cronogramaId);
}
