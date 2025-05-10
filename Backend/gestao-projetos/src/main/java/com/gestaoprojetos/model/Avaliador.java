package com.gestaoprojetos.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@Entity(name = "_avaliador")
public class Avaliador extends Pessoa implements Serializable {
}
