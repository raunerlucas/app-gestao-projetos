package com.gestaoprojetos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "_premio")
public class Premio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome é obrigatório!")
    @Column(length = 150)
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(length = 255)
    private String descricao;

    @NotBlank(message = "A data final é obrigatória!")
    @Column(name = "ano_edicao")
    @Digits(integer = 4, fraction = 0, message = "Ano deve ter no máximo 4 dígitos")
    private Integer anoEdicao;

    @NotNull(message = "O valor é obrigatório!")
    @ManyToOne
    @JoinColumn(name = "cronograma_id", nullable = false)
    private Cronograma cronograma;
}