package com.gestaoprojetos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
@Entity(name = "_autor")
public class Autor extends Pessoa implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    @JsonIgnore
    @ManyToMany(mappedBy = "autores")
    private List<Projeto> projetos = new ArrayList<>();
}
