package com.gestaoprojetos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
@Entity(name = "_autor")
public class Autor extends Pessoa implements Serializable {

    @ManyToMany(mappedBy = "autores")
    private List<Projeto> projetos;

}
