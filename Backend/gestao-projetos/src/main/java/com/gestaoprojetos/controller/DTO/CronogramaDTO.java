package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Cronograma;
import com.gestaoprojetos.model.Cronograma.StatusCronograma;
import com.gestaoprojetos.model.Premio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

public class CronogramaDTO {

    public CronogramaResponseDTO toCronogramaResponseDTO(Cronograma cronograma) {
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

    public Cronograma toCronograma(CronogramaRequestDTO cronogramaRequestDTO) {
        Cronograma cronograma = new Cronograma();
        cronograma.setDataInicio(Date.valueOf(cronogramaRequestDTO.getDataInicio()).toLocalDate());
        cronograma.setDataFim(Date.valueOf(cronogramaRequestDTO.getDataFim()).toLocalDate());
        cronograma.setDescricao(cronogramaRequestDTO.getDescricao());
        cronograma.setStatusById(cronogramaRequestDTO.statusId);
        if (cronogramaRequestDTO.getPremiosIds() != null) {
            cronograma.setPremios(cronogramaRequestDTO.getPremiosIds().stream()
                    .map(id -> {
                        Premio premio = new Premio();
                        premio.setId(id);
                        return premio;
                    }).toList());
        }
        return cronograma;
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
        private Long statusId;
        private List<Long> premiosIds;
    }
}

