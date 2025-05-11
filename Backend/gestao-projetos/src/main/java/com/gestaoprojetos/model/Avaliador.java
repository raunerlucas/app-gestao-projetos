package com.gestaoprojetos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString(callSuper = true)
@Entity(name = "_avaliador")
public class Avaliador extends Pessoa implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    private String parecer;

    private Double nota;

    private Date dataAvaliacao;

    @ManyToOne
    @JoinColumn(name = "avaliador_id")
    private Avaliacao avaliador;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;
}
