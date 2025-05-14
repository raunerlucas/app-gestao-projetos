package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Premio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremioRepository extends JpaRepository<Premio, Long> {
}
