package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Pessoa;
import com.gestaoprojetos.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public final class UsuarioDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private UsuarioDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UsuarioResponseDTO {
        private Long id;
        private String username;
        private PessoaDTO.PessoaResumoDTO pessoa;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UsuarioRequestDTO {
        private String username;
        private String password;
        private Long pessoaId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UsuarioResumoDTO {
        private Long id;
        private String username;
    }

    /**
     * Converte entidade Usuario para UsuarioResponseDTO.
     *
     * @param usuario Entidade Usuario completa
     * @return DTO para resposta da API
     */
    public static UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPessoa() != null ? PessoaDTO.toPessoaResumoDTO(usuario.getPessoa()) : null
        );
    }

    /**
     * Converte UsuarioRequestDTO para entidade Usuario.
     * A pessoa será apenas um objeto temporário com ID.
     *
     * @param usuarioRequestDTO DTO com os dados da requisição
     * @return Usuario com dados básicos (pessoa deve ser validada no service)
     */
    public static Usuario toUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        if (usuarioRequestDTO == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioRequestDTO.getUsername());
        usuario.setPassword(usuarioRequestDTO.getPassword());

        if (usuarioRequestDTO.getPessoaId() != null) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(usuarioRequestDTO.getPessoaId());
            usuario.setPessoa(pessoa);
        }

        return usuario;
    }

    /**
     * Converte entidade Usuario para UsuarioResumoDTO.
     *
     * @param usuario Entidade Usuario completa
     * @return DTO resumo para uso em outros DTOs
     */
    public static UsuarioResumoDTO toUsuarioResumoDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResumoDTO(
                usuario.getId(),
                usuario.getUsername()
        );
    }
}
