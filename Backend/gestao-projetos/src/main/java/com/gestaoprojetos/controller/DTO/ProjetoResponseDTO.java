package com.gestaoprojetos.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor


public class ProjetoResponseDTO {
    private Long id;
    private String titulo;
    private String resumo;
    private LocalDate dataEnvio;
    private String areaTematica;
    private List<AutorResumoDTO> autores;
    private List<AvaliacaoResumoDTO> avaliacoes;
}
