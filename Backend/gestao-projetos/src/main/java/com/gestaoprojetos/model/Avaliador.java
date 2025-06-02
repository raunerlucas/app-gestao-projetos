package com.gestaoprojetos.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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

    /**
     * Um Avaliador faz várias Avaliações.
     * O atributo mappedBy="avaliador" indica que a FK (avaliador_id) está na entidade Avaliacao.
     * Portanto não usamos @JoinColumn aqui.
     */
    @OneToMany(mappedBy = "avaliador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>();
}
