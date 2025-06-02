package com.gestaoprojetos.service;


import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Serviço para gerenciar Usuários.
 * - CRUD básico herdado de BasicRepositoryIMP
 * - registre e busque usuário por username
 * - faz hashing de senha usando PasswordEncoder
 */
@Service
@Transactional
public class UsuarioServiceImpl
        extends BasicRepositoryIMP<UsuarioRepository, Usuario, Long> {

    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra um novo Usuário: valida campos, assegura que username não exista,
     * associa a Pessoa já existente e faz hash da senha antes de salvar.
     *
     * @param usuario Objeto Usuario com username, password (plain) e Pessoa associada.
     * @return Usuario persistido (com senha em hash).
     * @throws BadRequestException se username já existir ou se algum campo for inválido.
     */
    public Usuario registrarUsuario(Usuario usuario) {
        // 1. Validações simples de campo
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new BadRequestException("O username é obrigatório e não pode ficar vazio.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new BadRequestException("A senha é obrigatória e não pode ficar vazia.");
        }
        if (usuario.getPessoa() == null || usuario.getPessoa().getId() == null) {
            throw new BadRequestException("O Usuario deve estar associado a uma Pessoa já existente.");
        }

        // 2. Verifica se já existe username igual
        Optional<Usuario> existente = getRepository()
                .findByUsername(usuario.getUsername().trim());
        if (existente.isPresent()) {
            throw new BadRequestException(
                    "Username já em uso: " + usuario.getUsername()
            );
        }

        // 3. Faz hash da senha com BCrypt
        String senhaHash = passwordEncoder.encode(usuario.getPassword().trim());
        usuario.setPassword(senhaHash);

        // 4. Persiste
        return save(usuario);
    }

    /**
     * Busca um usuário por username, ou lança ResourceNotFoundException.
     *
     * @param username nome de login
     * @return Usuario encontrado
     * @throws ResourceNotFoundException se não achar
     */
    public Usuario buscarPorUsername(String username) {
        return getRepository()
                .findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado: " + username)
                );
    }

    /**
     * Verifica se um usuário com aquele username já existe.
     */
    public boolean existsByUsername(String username) {
        return getRepository().findByUsername(username).isPresent();
    }
}
