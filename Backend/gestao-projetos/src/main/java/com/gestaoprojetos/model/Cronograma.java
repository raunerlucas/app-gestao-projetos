package com.gestaoprojetos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "_cronograma")
public class Cronograma implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data de início é obrigatória")
    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatória")
    @Column(name = "data_fim")
    private LocalDate dataFim;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(length = 255)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusCronograma status = StatusCronograma.NAO_INICIADO;

    @OneToMany(mappedBy = "cronograma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Premio> premios = new ArrayList<>();

    public enum StatusCronograma {
        NAO_INICIADO,
        EM_ANDAMENTO,
        CONCLUIDO,
        ATRASADO,
        CANCELADO
    }
}