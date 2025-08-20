package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Avaliacao;
import com.gestaoprojetos.model.Avaliador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public final class AvaliadorDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private AvaliadorDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AvaliadorResponseDTO {
        private Long id;
        private String nome;
        private String cpf;
        private String telefone;
        private String email;
        private List<Long> avaliacoesIds;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AvaliadorRequestDTO {
        private String nome;
        private String cpf;
        private String telefone;
        private String email;
        private List<Long> avaliacoesIds;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AvaliadorResumoDTO {
        private Long id;
        private String nome;
        private String email;
    }

    /**
     * Converte entidade Avaliador para AvaliadorResponseDTO.
     *
     * @param avaliador Entidade Avaliador completa
     * @return DTO para resposta da API
     */
    public static AvaliadorResponseDTO toAvaliadorResponseDTO(Avaliador avaliador) {
        if (avaliador == null) {
            return null;
        }

        return new AvaliadorResponseDTO(
                avaliador.getId(),
                avaliador.getNome(),
                avaliador.getCpf(),
                avaliador.getTelefone(),
                avaliador.getEmail(),
                avaliador.getAvaliacoes() != null ? avaliador.getAvaliacoes().stream()
                        .map(Avaliacao::getId)
                        .toList() : List.of()
        );
    }

    /**
     * Converte AvaliadorRequestDTO para entidade Avaliador.
     * As avaliações serão apenas objetos temporários com IDs.
     *
     * @param avaliadorRequestDTO DTO com os dados da requisição
     * @return Avaliador com dados básicos (avaliações devem ser validadas no service)
     */
    public static Avaliador toAvaliador(AvaliadorRequestDTO avaliadorRequestDTO) {
        if (avaliadorRequestDTO == null) {
            return null;
        }

        Avaliador avaliador = new Avaliador();
        avaliador.setNome(avaliadorRequestDTO.getNome());
        avaliador.setCpf(avaliadorRequestDTO.getCpf());
        avaliador.setTelefone(avaliadorRequestDTO.getTelefone());
        avaliador.setEmail(avaliadorRequestDTO.getEmail());

        if (avaliadorRequestDTO.getAvaliacoesIds() != null && !avaliadorRequestDTO.getAvaliacoesIds().isEmpty()) {
            avaliador.setAvaliacoes(avaliadorRequestDTO.getAvaliacoesIds().stream()
                    .map(id -> {
                        Avaliacao avaliacao = new Avaliacao();
                        avaliacao.setId(id);
                        return avaliacao;
                    }).toList());
        }

        return avaliador;
    }

    /**
     * Converte entidade Avaliador para AvaliadorResumoDTO.
     *
     * @param avaliador Entidade Avaliador completa
     * @return DTO resumo para uso em outros DTOs
     */
    public static AvaliadorResumoDTO toAvaliadorResumoDTO(Avaliador avaliador) {
        if (avaliador == null) {
            return null;
        }

        return new AvaliadorResumoDTO(
                avaliador.getId(),
                avaliador.getNome(),
                avaliador.getEmail()
        );
    }
}
