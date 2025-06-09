package com.gestaoprojetos.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "_avaliador")
public class Avaliador extends Pessoa implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    /**
     * Um Avaliador faz várias Avaliações.
     * O atributo mappedBy="avaliador" indica que a FK (avaliador_id) está na entidade Avaliacao.
     * Portanto não usamos @JoinColumn aqui.
     */
    @OneToMany(mappedBy = "avaliador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public Avaliador(Long id, String nome, String cpf, String email, String telefone, List<Avaliacao> avaliacoes) {
        super(id, nome, cpf, email, telefone);
        this.avaliacoes = avaliacoes != null ? avaliacoes : new ArrayList<>();
    }

}
