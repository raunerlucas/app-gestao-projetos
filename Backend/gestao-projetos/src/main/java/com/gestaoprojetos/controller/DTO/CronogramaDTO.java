package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Cronograma;
import com.gestaoprojetos.model.Cronograma.StatusCronograma;
import com.gestaoprojetos.model.Premio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

public final class CronogramaDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private CronogramaDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CronogramaResponseDTO {
        private Long id;
        private String dataInicio;
        private String dataFim;
        private String descricao;
        private StatusCronograma status;
        private List<Long> premiosIds;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CronogramaRequestDTO {
        private String dataInicio;
        private String dataFim;
        private String descricao;
        private StatusCronograma status;
        private List<Long> premiosIds;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CronogramaResumoDTO {
        private Long id;
        private String descricao;
        private StatusCronograma status;
    }

    /**
     * Converte entidade Cronograma para CronogramaResponseDTO.
     *
     * @param cronograma Entidade Cronograma completa
     * @return DTO para resposta da API
     */
    public static CronogramaResponseDTO toCronogramaResponseDTO(Cronograma cronograma) {
        if (cronograma == null) {
            return null;
        }

        return new CronogramaResponseDTO(
                cronograma.getId(),
                cronograma.getDataInicio().toString(),
                cronograma.getDataFim().toString(),
                cronograma.getDescricao(),
                cronograma.getStatus(),
                cronograma.getPremios() != null ? cronograma.getPremios().stream()
                        .map(Premio::getId)
                        .toList() : List.of()
        );
    }

    /**
     * Converte CronogramaRequestDTO para entidade Cronograma.
     * Os prêmios serão apenas objetos temporários com IDs.
     *
     * @param cronogramaRequestDTO DTO com os dados da requisição
     * @return Cronograma com dados básicos (prêmios devem ser validados no service)
     */
    public static Cronograma toCronograma(CronogramaRequestDTO cronogramaRequestDTO) {
        if (cronogramaRequestDTO == null) {
            return null;
        }

        Cronograma cronograma = new Cronograma();
        cronograma.setDataInicio(Date.valueOf(cronogramaRequestDTO.getDataInicio()).toLocalDate());
        cronograma.setDataFim(Date.valueOf(cronogramaRequestDTO.getDataFim()).toLocalDate());
        cronograma.setDescricao(cronogramaRequestDTO.getDescricao());

        if (cronogramaRequestDTO.getStatus() != null) {
            cronograma.setStatus(cronogramaRequestDTO.getStatus());
        }

        if (cronogramaRequestDTO.getPremiosIds() != null && !cronogramaRequestDTO.getPremiosIds().isEmpty()) {
            cronograma.setPremios(cronogramaRequestDTO.getPremiosIds().stream()
                    .map(id -> {
                        Premio premio = new Premio();
                        premio.setId(id);
                        return premio;
                    }).toList());
        }

        return cronograma;
    }

    /**
     * Converte entidade Cronograma para CronogramaResumoDTO.
     *
     * @param cronograma Entidade Cronograma completa
     * @return DTO resumo para uso em outros DTOs
     */
    public static CronogramaResumoDTO toCronogramaResumoDTO(Cronograma cronograma) {
        if (cronograma == null) {
            return null;
        }

        return new CronogramaResumoDTO(
                cronograma.getId(),
                cronograma.getDescricao(),
                cronograma.getStatus()
        );
    }
}
