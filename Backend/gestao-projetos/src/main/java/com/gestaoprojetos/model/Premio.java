package com.gestaoprojetos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity(name = "_premio")
public class Premio implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório e não pode ficar em branco")
    @Column(length = 150, nullable = false)
    private String nome;

    @NotBlank(message = "A descrição é obrigatória e não pode ficar em branco")
    @Column(length = 255, nullable = false)
    private String descricao;

    @NotNull(message = "O ano da edição é obrigatório")
    @Digits(integer = 4, fraction = 0, message = "Ano deve ter no máximo 4 dígitos numéricos")
    @Min(value = 0, message = "Ano da edição não pode ser negativo")
    @Column(name = "ano_edicao", nullable = false)
    private Integer anoEdicao;

    @NotNull(message = "O cronograma é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cronograma_id", nullable = false)
    private Cronograma cronograma;
}