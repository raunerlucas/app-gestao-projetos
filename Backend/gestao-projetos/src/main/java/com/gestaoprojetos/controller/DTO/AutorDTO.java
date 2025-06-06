package com.gestaoprojetos.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AutorDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AutorResponseDTO {
        private Long id;
        private String nome;
        private String telefone;
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AutorRequestDTO {
        private String nome;
        private String CPF;
        private String email;
        private String telefone;
    }
}

