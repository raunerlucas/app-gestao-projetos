package com.gestaoprojetos.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

// DTO para representar uma avaliação de forma resumida, com ID, nota e parecer.
public class AvaliacaoResumoDTO {
    private Long id;
    private Double nota;
    private String parecer;
}
