package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Usuario;
import lombok.Data;

@Data
public class UserResponseDTO {
    private final Long idUser;
    private final String userName;
    private final Long pessoaId;

    public UserResponseDTO(Usuario user) {
        this.idUser = user.getId();
        this.userName = user.getUsername();
        this.pessoaId = user.getPessoa() != null ? user.getPessoa().getId() : null;
    }
}