package com.gestaoprojetos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public abstract class Pessoa {
    @Id
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
}
