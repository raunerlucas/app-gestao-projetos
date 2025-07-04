package com.gestaoprojetos.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PessoaDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PessoaResponseDTO {
        private Long id;
        private String nome;
        private String telefone;
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PessoaRequestDTO {
        private String nome;
        private String CPF;
        private String email;
        private String telefone;
    }
}

