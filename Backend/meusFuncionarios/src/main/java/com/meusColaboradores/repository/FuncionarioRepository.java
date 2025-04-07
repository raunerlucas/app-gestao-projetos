package com.meusColaboradores.repository;

import com.meusColaboradores.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    // Aqui você pode adicionar métodos personalizados, se necessário
    // Exemplo: List<Funcionario> findByNome(String nome);
}
