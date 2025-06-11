package com.gestaoprojetos.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

// DTO para representar um autor de forma resumida, com ID e nome.
public class AutorResumoDTO {
    private Long id;
    private String nome;
}