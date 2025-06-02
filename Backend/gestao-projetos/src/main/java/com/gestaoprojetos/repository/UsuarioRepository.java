package com.gestaoprojetos.repository;

import com.gestaoprojetos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório para Usuario.
 * Além dos métodos CRUD padrão, definimos findByUsername para autenticar.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Busca um usuário pelo username (único).
     * O retorno Optional facilita tratar “não encontrado” em camadas superiores.
     */
    Optional<Usuario> findByUsername(String username);
}

