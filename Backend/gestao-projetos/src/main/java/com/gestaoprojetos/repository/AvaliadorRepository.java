package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Avaliador;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliadorRepository extends JpaRepository<Avaliador, Long> {
    boolean existsByCpf(@Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$",
            message = "CPF deve estar no formato 000.000.000-00 ou 00000000000") String cpf);
}
