package com.gestaoprojetos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity(name = "_projeto")
@NoArgsConstructor
public class Projeto implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título do projeto é obrigatório")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "O resumo do projeto é obrigatório")
    @Column(nullable = false)
    private String resumo;

    @NotNull(message = "A data de envio é obrigatória")
    @Column(name = "data_envio", nullable = false)
    private LocalDate dataEnvio;

    @NotBlank(message = "A área temática do projeto é obrigatória")
    @Column(name = "area_tematica", nullable = false)
    private String areaTematica;

    /**
     * Um projeto possui várias avaliações.
     * O atributo mappedBy = "projeto" indica que a FK está em Avaliacao.projeto.
     * Cascade.ALL para que salvar/remover Projeto afete as Avaliacoes.
     * orphanRemoval = true para excluir avaliações que saem da lista.
     */
    @OneToMany(
            mappedBy = "projeto",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    /**
     * Muitos autores podem estar em muitos projetos (N:N).
     * A tabela intermediária se chama "_projeto_autor".
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "_projeto_autor",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();
    public Projeto(
            Long id,
            String titulo,
            String resumo,
            LocalDate dataEnvio,
            String areaTematica,
            List<Autor> autores,
            List<Avaliacao> avaliacoes
    ) {
        this.id = id;
        this.titulo = titulo;
        this.resumo = resumo;
        this.dataEnvio = dataEnvio;
        this.areaTematica = areaTematica;
        this.autores = autores != null ? autores : new ArrayList<>();
        this.avaliacoes = avaliacoes != null ? avaliacoes : new ArrayList<>();
    }
}
