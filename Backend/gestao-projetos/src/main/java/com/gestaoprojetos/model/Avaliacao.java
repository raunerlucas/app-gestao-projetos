package com.gestaoprojetos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity(name = "_avaliacao")
public class Avaliacao implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String parecer;

    @Column(nullable = false)
    private Double nota;

    @JoinColumn(name = "data_avaliacao")
    private LocalDate dataAvaliacao;

    @OneToOne(optional = false)
    @EqualsAndHashCode.Include
    private  Avaliador avaliador;

    @ManyToOne(optional = false)
    private Status status;

    @ManyToOne(optional = false)
    private Projeto projeto;
}
