package com.gestaoprojetos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
@Entity(name = "_avaliador")
public class Avaliador extends Pessoa implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    private String parecer;

    private Double nota;

    private Date dataAvaliacao;

    @OneToMany(mappedBy = "avaliador")
    @JoinColumn(name = "avaliador_id")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "projeto_id")
    private List<Projeto> projetos = new ArrayList<>();
}
