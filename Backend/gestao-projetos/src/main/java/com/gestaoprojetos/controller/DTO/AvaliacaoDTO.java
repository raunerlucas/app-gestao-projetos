package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Avaliacao;
import com.gestaoprojetos.model.Avaliador;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public final class AvaliacaoDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private AvaliacaoDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AvaliacaoResponseDTO {
        private Long id;
        private String parecer;
        private Double nota;
        private LocalDate dataAvaliacao;
        private AvaliadorDTO.AvaliadorResumoDTO avaliador;
        private ProjetoDTO.ProjetoResumoDTO projeto;
        private StatusDTO.StatusResumoDTO status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AvaliacaoRequestDTO {
        private String parecer;
        private Double nota;
        private LocalDate dataAvaliacao;
        private Long avaliadorId;
        private Long projetoId;
        private Long statusId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AvaliacaoResumoDTO {
        private Long id;
        private Double nota;
        private String parecer;
    }

    /**
     * Converte entidade Avaliacao para AvaliacaoResponseDTO.
     *
     * @param avaliacao Entidade Avaliacao completa
     * @return DTO para resposta da API
     */
    public static AvaliacaoResponseDTO toAvaliacaoResponseDTO(Avaliacao avaliacao) {
        if (avaliacao == null) {
            return null;
        }

        return new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getParecer(),
                avaliacao.getNota(),
                avaliacao.getDataAvaliacao(),
                avaliacao.getAvaliador() != null ? AvaliadorDTO.toAvaliadorResumoDTO(avaliacao.getAvaliador()) : null,
                avaliacao.getProjeto() != null ? ProjetoDTO.toProjetoResumoDTO(avaliacao.getProjeto()) : null,
                StatusDTO.toStatusResumoDTO(avaliacao.getStatus())
        );
    }

    /**
     * Converte AvaliacaoRequestDTO para entidade Avaliacao.
     * O avaliador, projeto e status serão apenas objetos temporários com IDs.
     *
     * @param avaliacaoRequestDTO DTO com os dados da requisição
     * @return Avaliacao com dados básicos (avaliador, projeto e status devem ser validados no service)
     */
    public static Avaliacao toAvaliacao(AvaliacaoRequestDTO avaliacaoRequestDTO) {
        if (avaliacaoRequestDTO == null) {
            return null;
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setParecer(avaliacaoRequestDTO.getParecer());
        avaliacao.setNota(avaliacaoRequestDTO.getNota());
        avaliacao.setDataAvaliacao(avaliacaoRequestDTO.getDataAvaliacao());

        if (avaliacaoRequestDTO.getAvaliadorId() != null) {
            Avaliador avaliador = new Avaliador();
            avaliador.setId(avaliacaoRequestDTO.getAvaliadorId());
            avaliacao.setAvaliador(avaliador);
        }

        if (avaliacaoRequestDTO.getProjetoId() != null) {
            Projeto projeto = new Projeto();
            projeto.setId(avaliacaoRequestDTO.getProjetoId());
            avaliacao.setProjeto(projeto);
        }

        if (avaliacaoRequestDTO.getStatusId() != null) {
            Status status = new Status();
            status.setId(avaliacaoRequestDTO.getStatusId());
            avaliacao.setStatus(status);
        }

        return avaliacao;
    }

    /**
     * Converte entidade Avaliacao para AvaliacaoResumoDTO.
     *
     * @param avaliacao Entidade Avaliacao completa
     * @return DTO resumo para uso em outros DTOs
     */
    public static AvaliacaoResumoDTO toAvaliacaoResumoDTO(Avaliacao avaliacao) {
        if (avaliacao == null) {
            return null;
        }

        return new AvaliacaoResumoDTO(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getParecer()
        );
    }
}
