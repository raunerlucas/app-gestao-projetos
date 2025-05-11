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

    private Long id;
    private String parecer;
    private double nota;
    private Date dataAvaliacao;

    @ManyToOne //  um avaliador pode ter várias avaliações, avalicao pertence a um avaliador
    @JoinColumn(name = "avaliador_id")
    private Avaliacao avaliador;

    @ManyToOne // avaliador pode avaliar vários projetos
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;
}
