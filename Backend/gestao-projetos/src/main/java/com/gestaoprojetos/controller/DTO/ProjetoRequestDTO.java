package com.gestaoprojetos.controller.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjetoRequestDTO {
    private String titulo;
    private String resumo;
    private LocalDate dataEnvio;
    private String areaTematica;
}
