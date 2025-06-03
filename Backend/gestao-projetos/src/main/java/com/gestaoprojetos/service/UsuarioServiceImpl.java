package com.gestaoprojetos.service;

import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioServiceImpl
        extends BasicRepositoryIMP<UsuarioRepository, Usuario, Long> {

    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new BadRequestException("O username é obrigatório e não pode ficar vazio.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new BadRequestException("A senha é obrigatória e não pode ficar vazia.");
        }
        if (usuario.getPessoa() == null || usuario.getPessoa().getId() == null) {
            throw new BadRequestException("O Usuario deve estar associado a uma Pessoa já existente.");
        }

        boolean jaExiste = getRepository()
                .findByUsername(usuario.getUsername().trim())
                .isPresent();
        if (jaExiste) {
            throw new BadRequestException("Username já em uso: " + usuario.getUsername());
        }

        String senhaHash = passwordEncoder.encode(usuario.getPassword().trim());
        usuario.setPassword(senhaHash);
        return save(usuario);
    }

    public Usuario buscarPorUsername(String username) {
        return getRepository()
                .findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado: " + username)
                );
    }
}
