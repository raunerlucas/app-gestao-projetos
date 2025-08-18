package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Cronograma;
import com.gestaoprojetos.model.Cronograma.StatusCronograma;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

public class CronogramaDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CronogramaResponseDTO {
        private Long id;
        private String dataInicio;
        private String dataFim;
        private String descricao;
        private StatusCronograma status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CronogramaRequestDTO {
        private String dataInicio;
        private String dataFim;
        private String descricao;
        private Long statusId;
    }

    public CronogramaResponseDTO toCronogramaResponseDTO(Cronograma cronograma) {
        return new CronogramaResponseDTO(
                cronograma.getId(),
                cronograma.getDataInicio().toString(),
                cronograma.getDataFim().toString(),
                cronograma.getDescricao(),
                cronograma.getStatus()
        );
    }

    public Cronograma toCronograma(CronogramaRequestDTO cronogramaRequestDTO) {
        Cronograma cronograma = new Cronograma();
        cronograma.setDataInicio(Date.valueOf(cronogramaRequestDTO.getDataInicio()).toLocalDate());
        cronograma.setDataFim(Date.valueOf(cronogramaRequestDTO.getDataFim()).toLocalDate());
        cronograma.setDescricao(cronogramaRequestDTO.getDescricao());
        cronograma.setStatusById(cronogramaRequestDTO.statusId);
        return cronograma;
    }
}

