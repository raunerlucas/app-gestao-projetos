package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Avaliacao;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public final class AssociacaoDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private AssociacaoDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssociacaoRequestDTO {
        @NotNull(message = "ID da avaliação não pode ser nulo")
        private Long avaliacaoId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssociacaoResponseDTO {
        private Long id;
        private Long avaliacaoId;
        private String status;
    }

    /**
     * Converte AssociacaoRequestDTO para objeto Avaliacao temporário com ID.
     *
     * @param associacaoRequestDTO DTO com ID da avaliação
     * @return Avaliacao temporária com apenas ID (deve ser validada no service)
     */
    public static Avaliacao toAvaliacao(AssociacaoRequestDTO associacaoRequestDTO) {
        if (associacaoRequestDTO == null || associacaoRequestDTO.getAvaliacaoId() == null) {
            return null;
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(associacaoRequestDTO.getAvaliacaoId());
        return avaliacao;
    }

    /**
     * Cria um DTO de resposta para associação.
     *
     * @param id ID da associação
     * @param avaliacaoId ID da avaliação associada
     * @param status Status da associação
     * @return DTO de resposta
     */
    public static AssociacaoResponseDTO toAssociacaoResponseDTO(Long id, Long avaliacaoId, String status) {
        return new AssociacaoResponseDTO(id, avaliacaoId, status);
    }
}
