package com.gestaoprojetos.service;

import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.repository.BasicRepositoryIMP;
import com.gestaoprojetos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioServiceIMP
        extends BasicRepositoryIMP<UsuarioRepository, Usuario, Long> {

    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceIMP(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra um novo usuário no sistema.
     * Valida o usuário e verifica se já existe um usuário com o mesmo username e pessoa.
     * Se não existir, cria um novo usuário com a senha criptografada.
     *
     * @param usuario O usuário a ser registrado
     * @return O usuário registrado
     */
    public Usuario registrarUsuario(Usuario usuario) {
        validaUser(usuario);

        if (validaExistencia(usuario)) {
            throw new BadRequestException("Usuario já existe: " + usuario.getUsername());
        }
        String senhaHash = passwordEncoder.encode(usuario.getPassword().trim());
        usuario.setPassword(senhaHash);
        return save(usuario);
    }

    /**
     * Verifica se já existe um usuário com o mesmo username e pessoa.
     *
     * @param usuario O usuário a ser verificado
     * @return true se já existir, false caso contrário
     */
    public Usuario buscarPorUsername(Usuario usuario) {
        String username = usuario.getUsername().trim();
        return getRepository()
                .findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado: " + username)
                );
    }

    /**
     * Busca um usuário pelo ID.
     * Se não encontrar, lança uma ResourceNotFoundException.
     *
     * @param id O ID do usuário a ser buscado
     * @return O usuário encontrado
     */
    public Usuario buscarPorId(Long id) throws ResourceNotFoundException {
        return getRepository()
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }

    /**
     * Verifica se já existe um usuário com o mesmo username e pessoa.
     *
     * @param usuario O usuário a ser verificado
     * @return true se já existir, false caso contrário
     */
    private Boolean validaExistencia(Usuario usuario) {
        return getRepository()
                .findByUsernameAndPessoaId(
                        usuario.getUsername().trim(),
                        usuario.getPessoa().getId()
                )
                .isPresent();

    }

    /**
     * Valida os campos obrigatórios do usuário.
     * Lança uma BadRequestException se algum campo obrigatório estiver vazio ou nulo.
     *
     * @param usuario O usuário a ser validado
     */
    private static void validaUser(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new BadRequestException("O username é obrigatório e não pode ficar vazio.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new BadRequestException("A senha é obrigatória e não pode ficar vazia.");
        }
        if (usuario.getPessoa() == null || usuario.getPessoa().getId() == null) {
            throw new BadRequestException("O Usuario deve estar associado a uma Pessoa já existente.");
        }
    }
}
