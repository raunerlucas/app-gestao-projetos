package com.gestaoprojetos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.time.LocalDate;
@NoArgsConstructor
@Data
@Entity(name = "_avaliacao")
public class Avaliacao implements Serializable {

    public Avaliacao(String parecer, Double nota) {
        this.parecer = parecer;
        this.nota = nota;
    }


    //[] TODO: Colocar os Validações de cada campo

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "O parecer é obrigatório")
    @Column(nullable = false, length = 1000)
    private String parecer;

    @NotNull(message = "A nota é obrigatória")
    @DecimalMin(value = "0.0", inclusive = true, message = "A nota deve ser no mínimo 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "A nota deve ser no máximo 10.0")
    @Column(nullable = false)
    private Double nota;

    @NotNull(message = "A data de avaliação é obrigatória")
    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate dataAvaliacao;

    @NotNull(message = "O avaliador é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "avaliador_id", nullable = false)
    @EqualsAndHashCode.Include
    private Avaliador avaliador;

    @NotNull(message = "O status é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @NotNull(message = "O projeto é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;
}
