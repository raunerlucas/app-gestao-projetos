package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Projeto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public final class ProjetoDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private ProjetoDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjetoResponseDTO {
        private Long id;
        private String titulo;
        private String resumo;
        private LocalDate dataEnvio;
        private String areaTematica;
        private List<AutorDTO.AutorResumoDTO> autores;
        private List<AvaliacaoDTO.AvaliacaoResumoDTO> avaliacoes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjetoRequestDTO {
        private String titulo;
        private String resumo;
        private LocalDate dataEnvio;
        private String areaTematica;
        private List<Long> autoresId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjetoResumoDTO {
        private Long id;
        private String titulo;
        private String areaTematica;
    }

    /**
     * Converte entidade Projeto para ProjetoResponseDTO.
     *
     * @param projeto Entidade Projeto completa
     * @return DTO para resposta da API
     */
    public static ProjetoResponseDTO toProjetoResponseDTO(Projeto projeto) {
        if (projeto == null) {
            return null;
        }

        return new ProjetoResponseDTO(
                projeto.getId(),
                projeto.getTitulo(),
                projeto.getResumo(),
                projeto.getDataEnvio(),
                projeto.getAreaTematica(),
                projeto.getAutores() != null ? projeto.getAutores().stream()
                        .map(AutorDTO::toAutorResumoDTO)
                        .toList() : List.of(),
                projeto.getAvaliacoes() != null ? projeto.getAvaliacoes().stream()
                        .map(AvaliacaoDTO::toAvaliacaoResumoDTO)
                        .toList() : List.of()
        );
    }

    /**
     * Converte ProjetoRequestDTO para entidade Projeto.
     * Os autores serão apenas objetos temporários com IDs.
     *
     * @param projetoRequestDTO DTO com os dados da requisição
     * @return Projeto com dados básicos (autores devem ser validados no service)
     */
    public static Projeto toProjeto(ProjetoRequestDTO projetoRequestDTO) {
        if (projetoRequestDTO == null) {
            return null;
        }

        Projeto projeto = new Projeto();
        projeto.setTitulo(projetoRequestDTO.getTitulo());
        projeto.setResumo(projetoRequestDTO.getResumo());
        projeto.setDataEnvio(projetoRequestDTO.getDataEnvio());
        projeto.setAreaTematica(projetoRequestDTO.getAreaTematica());

        if (projetoRequestDTO.getAutoresId() != null && !projetoRequestDTO.getAutoresId().isEmpty()) {
            projeto.setAutores(projetoRequestDTO.getAutoresId().stream()
                    .map(id -> {
                        Autor autor = new Autor();
                        autor.setId(id);
                        return autor;
                    }).toList());
        }

        return projeto;
    }

    /**
     * Converte entidade Projeto para ProjetoResumoDTO.
     *
     * @param projeto Entidade Projeto completa
     * @return DTO resumo para uso em outros DTOs
     */
    public static ProjetoResumoDTO toProjetoResumoDTO(Projeto projeto) {
        if (projeto == null) {
            return null;
        }

        return new ProjetoResumoDTO(
                projeto.getId(),
                projeto.getTitulo(),
                projeto.getAreaTematica()
        );
    }
}
