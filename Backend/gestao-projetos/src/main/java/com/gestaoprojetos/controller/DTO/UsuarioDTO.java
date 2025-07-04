package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UsuarioDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UsuarioResponseDTO {
        private Long idUser;
        private String userName;
        private Long pessoaId;

        public UsuarioResponseDTO(Usuario usuario) {
            this.idUser = usuario.getId();
            this.userName = usuario.getUsername();
            this.pessoaId = usuario.getPessoa() != null ? usuario.getPessoa().getId() : null;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UsuarioRequestDTO {
        private String username;
        private String password;
        private Long pessoaId;
    }

}
