package com.gestaoprojetos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "_pessoa")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Pessoa implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    @Id
    @SequenceGenerator(
            name = "pessoa_seq",           // Nome do gerador
            sequenceName = "pessoa_seq",   // Nome da sequência no banco
            allocationSize = 1             // Incremento (ajuste conforme necessário)
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_seq")
    private Long id;

    @Column(length = 150)
    private String nome;

    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$",
            message = "CPF deve estar no formato 000.000.000-00 ou 00000000000")
    private String cpf;

    @Pattern(regexp = "^(\\d{4,5})[-\\s]?(\\d{4})$",
            message = "Telefone deve estar no formato XXXXX-XXXX ou XXXX-XXXX")
    private String telefone;

    @Pattern(regexp = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$",
            message = "Email deve estar no formato exp@exp.exp")
    private String email;
}

