package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.controller.DTO.CronogramaDTO.CronogramaResponseDTO;
import com.gestaoprojetos.model.Cronograma;
import com.gestaoprojetos.model.Premio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PremioDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PremioResponseDTO {
        private Long id;
        private String nome;
        private String descricao;
        private Integer anoEdicao;
        private CronogramaResponseDTO cronograma;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PremioRequestDTO {
        private String nome;
        private String descricao;
        private Integer anoEdicao;
        private Long cronogramaId;
    }

    public Premio toPremio(PremioRequestDTO premioRequestDTO, Cronograma cronograma) {
        Premio premio = new Premio();
        premio.setNome(premioRequestDTO.getNome());
        premio.setDescricao(premioRequestDTO.getDescricao());
        premio.setAnoEdicao(premioRequestDTO.getAnoEdicao());
        premio.setCronograma(cronograma);
        return premio;
    }

    public PremioResponseDTO toPremioResponseDTO(Premio premio) {
        return new PremioResponseDTO(
                premio.getId(),
                premio.getNome(),
                premio.getDescricao(),
                premio.getAnoEdicao(),
                new CronogramaResponseDTO(
                        premio.getCronograma().getId(),
                        premio.getCronograma().getDataInicio().toString(),
                        premio.getCronograma().getDataFim().toString(),
                        premio.getCronograma().getDescricao(),
                        premio.getCronograma().getStatus(),
                        null
                )
        );
    }
}

