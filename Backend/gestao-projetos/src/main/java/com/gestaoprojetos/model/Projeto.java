package com.gestaoprojetos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.time.LocalDate;
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
    private String titulo;

    @NotBlank(message = "O resumo do projeto é obrigatório")
    private String resumo;

    @NonNull
    private LocalDate dataEnvio;

    @NotBlank(message = "A área temática do projeto é obrigatória")
    private String areaTematica;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes;

    @ManyToMany
    @JoinTable(
            name = "_projeto_autor",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;
}
