package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public final class StatusDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private StatusDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusResponseDTO {
        private Long id;
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusRequestDTO {
        private Long id;
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusResumoDTO {
        private Long id;
        private String description;
    }

    /**
     * Converte entidade Status para StatusResponseDTO.
     *
     * @param status Entidade Status completa
     * @return DTO para resposta da API
     */
    public static StatusResponseDTO toStatusResponseDTO(Status status) {
        if (status == null) {
            return null;
        }

        return new StatusResponseDTO(
                status.getId(),
                status.getDescription()
        );
    }

    /**
     * Converte StatusRequestDTO para entidade Status.
     *
     * @param statusRequestDTO DTO com os dados da requisição
     * @return Status com dados da requisição
     */
    public static Status toStatus(StatusRequestDTO statusRequestDTO) {
        if (statusRequestDTO == null) {
            return null;
        }

        return new Status(
                statusRequestDTO.getId(),
                statusRequestDTO.getDescription()
        );
    }

    /**
     * Converte entidade Status para StatusResumoDTO.
     *
     * @param status Entidade Status completa
     * @return DTO resumo para uso em outros DTOs
     */
    public static StatusResumoDTO toStatusResumoDTO(Status status) {
        if (status == null) {
            return null;
        }

        return new StatusResumoDTO(
                status.getId(),
                status.getDescription()
        );
    }
}
