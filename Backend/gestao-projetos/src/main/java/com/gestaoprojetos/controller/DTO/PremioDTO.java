package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.controller.DTO.CronogramaDTO.CronogramaResponseDTO;
import com.gestaoprojetos.model.Cronograma;
import com.gestaoprojetos.model.Premio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public final class PremioDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private PremioDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

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

    /**
     * Converte PremioRequestDTO para entidade Premio.
     * Note que o cronograma deve ser definido posteriormente com o objeto completo.
     *
     * @param premioRequestDTO DTO com os dados da requisição
     * @return Premio com dados básicos (cronograma será null e deve ser definido no service)
     */
    public static Premio toPremio(PremioRequestDTO premioRequestDTO) {
        if (premioRequestDTO == null) {
            return null;
        }

        Premio premio = new Premio();
        premio.setNome(premioRequestDTO.getNome());
        premio.setDescricao(premioRequestDTO.getDescricao());
        premio.setAnoEdicao(premioRequestDTO.getAnoEdicao());

        // Criar um objeto Cronograma temporário apenas com o ID
        // O service será responsável por buscar e definir o cronograma completo
        if (premioRequestDTO.getCronogramaId() != null) {
            Cronograma cronograma = new Cronograma();
            cronograma.setId(premioRequestDTO.getCronogramaId());
            premio.setCronograma(cronograma);
        }

        return premio;
    }

    /**
     * Converte entidade Premio para PremioResponseDTO.
     *
     * @param premio Entidade Premio completa
     * @return DTO para resposta da API
     */
    public static PremioResponseDTO toPremioResponseDTO(Premio premio) {
        if (premio == null) {
            return null;
        }

        return new PremioResponseDTO(
                premio.getId(),
                premio.getNome(),
                premio.getDescricao(),
                premio.getAnoEdicao(),
                premio.getCronograma() != null ? new CronogramaResponseDTO(
                        premio.getCronograma().getId(),
                        premio.getCronograma().getDataInicio().toString(),
                        premio.getCronograma().getDataFim().toString(),
                        premio.getCronograma().getDescricao(),
                        premio.getCronograma().getStatus(),
                        null
                ) : null
        );
    }
}
