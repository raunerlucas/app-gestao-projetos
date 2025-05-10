package com.gestaoprojetos.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString(callSuper = true)
@Entity(name = "_autor")
public class Autor extends Pessoa implements Serializable {
}
