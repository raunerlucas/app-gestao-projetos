package com.meusColaboradores.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Funcionario {
    // TODO: Implementar atribustos de Funcionario

    @Id
    private Long id;

}
