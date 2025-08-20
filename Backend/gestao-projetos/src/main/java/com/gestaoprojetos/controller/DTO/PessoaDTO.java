package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public final class PessoaDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private PessoaDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PessoaResponseDTO {
        private Long id;
        private String nome;
        private String cpf;
        private String telefone;
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PessoaRequestDTO {
        private String nome;
        private String cpf;
        private String email;
        private String telefone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PessoaResumoDTO {
        private Long id;
        private String nome;
        private String email;
    }

    /**
     * Converte entidade Pessoa para PessoaResponseDTO.
     *
     * @param pessoa Entidade Pessoa completa
     * @return DTO para resposta da API
     */
    public static PessoaResponseDTO toPessoaResponseDTO(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        return new PessoaResponseDTO(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getTelefone(),
                pessoa.getEmail()
        );
    }

    /**
     * Converte PessoaRequestDTO para entidade Pessoa.
     *
     * @param pessoaRequestDTO DTO com os dados da requisição
     * @return Pessoa com dados da requisição
     */
    public static Pessoa toPessoa(PessoaRequestDTO pessoaRequestDTO) {
        if (pessoaRequestDTO == null) {
            return null;
        }

        return Pessoa.builder()
                .nome(pessoaRequestDTO.getNome())
                .cpf(pessoaRequestDTO.getCpf())
                .telefone(pessoaRequestDTO.getTelefone())
                .email(pessoaRequestDTO.getEmail())
                .build();
    }

    /**
     * Converte entidade Pessoa para PessoaResumoDTO.
     *
     * @param pessoa Entidade Pessoa completa
     * @return DTO resumo para uso em outros DTOs
     */
    public static PessoaResumoDTO toPessoaResumoDTO(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        return new PessoaResumoDTO(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getEmail()
        );
    }
}
