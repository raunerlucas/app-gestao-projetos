package com.gestaoprojetos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity(name = "_avaliacao")

public class Avaliacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @Column(nullable = false)
    private String parecer;

    @Column(nullable = false)
    private Double nota;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private String date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "avaliador_id")
    private LocalDate dataAvaliacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "avaliador_id")
    private  Avaliador avaliador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;
}
