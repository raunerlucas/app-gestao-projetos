package com.gestaoprojetos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "_autor")
public class Autor extends Pessoa implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    @ManyToMany(mappedBy = "autores")
    private List<Projeto> projetos = new ArrayList<>();

    public Autor(Long id, String nome, String CPF, String email, String telefone, List<Projeto> projetos) {
        super(id, nome, CPF, email, telefone);
        this.projetos = projetos != null ? projetos : new ArrayList<>();
    }
}
