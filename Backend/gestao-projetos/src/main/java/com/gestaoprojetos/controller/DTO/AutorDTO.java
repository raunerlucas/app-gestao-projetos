package com.gestaoprojetos.controller.DTO;

import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Projeto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public final class AutorDTO {

    // Construtor privado para impedir instanciação (classe utilitária)
    private AutorDTO() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não deve ser instanciada");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AutorResponseDTO {
        private Long id;
        private String nome;
        private String cpf;
        private String telefone;
        private String email;
        private List<Long> projetosIds;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AutorRequestDTO {
        private String nome;
        private String cpf;
        private String telefone;
        private String email;
        private List<Long> projetosIds;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AutorResumoDTO {
        private Long id;
        private String nome;
    }

    /**
     * Converte entidade Autor para AutorResponseDTO.
     *
     * @param autor Entidade Autor completa
     * @return DTO para resposta da API
     */
    public static AutorResponseDTO toAutorResponseDTO(Autor autor) {
        if (autor == null) {
            return null;
        }

        return new AutorResponseDTO(
                autor.getId(),
                autor.getNome(),
                autor.getCpf(),
                autor.getTelefone(),
                autor.getEmail(),
                autor.getProjetos() != null ? autor.getProjetos().stream()
                        .map(Projeto::getId)
                        .toList() : List.of()
        );
    }

    /**
     * Converte AutorRequestDTO para entidade Autor.
     * Os projetos serão apenas objetos temporários com IDs.
     *
     * @param autorRequestDTO DTO com os dados da requisição
     * @return Autor com dados básicos (projetos devem ser validados no service)
     */
    public static Autor toAutor(AutorRequestDTO autorRequestDTO) {
        if (autorRequestDTO == null) {
            return null;
        }

        Autor autor = new Autor();
        autor.setNome(autorRequestDTO.getNome());
        autor.setCpf(autorRequestDTO.getCpf());
        autor.setTelefone(autorRequestDTO.getTelefone());
        autor.setEmail(autorRequestDTO.getEmail());

        if (autorRequestDTO.getProjetosIds() != null && !autorRequestDTO.getProjetosIds().isEmpty()) {
            autor.setProjetos(autorRequestDTO.getProjetosIds().stream()
                    .map(id -> {
                        Projeto projeto = new Projeto();
                        projeto.setId(id);
                        return projeto;
                    }).toList());
        }

        return autor;
    }

    /**
     * Converte entidade Autor para AutorResumoDTO.
     *
     * @param autor Entidade Autor completa
     * @return DTO resumo para uso em outros DTOs
     */
    public static AutorResumoDTO toAutorResumoDTO(Autor autor) {
        if (autor == null) {
            return null;
        }

        return new AutorResumoDTO(
                autor.getId(),
                autor.getNome()
        );
    }
}
