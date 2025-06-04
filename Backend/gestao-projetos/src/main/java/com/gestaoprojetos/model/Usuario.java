package com.gestaoprojetos.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entidade Usuario para autenticação.
 * Cada Usuario referencia exatamente uma Pessoa (Autor OU Avaliador),
 * garantindo que o mesmo login não sirva a mais de uma Pessoa diferente.
 */
@Data
@NoArgsConstructor
@Entity(name = "_usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O username é obrigatório")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * O password será armazenado como hash (BCrypt).
     * Jamais exibir esse campo em outputs JSON.
     */
    @JsonIgnore
    @NotBlank(message = "A senha é obrigatória")
    @Column(nullable = false)
    private String password;

    /**
     * Relacionamento OneToOne com Pessoa (pode ser Autor ou Avaliador).
     * A strategy TABLE_PER_CLASS faz com que Autor e Avaliador sejam tabelas
     * separadas; ao referenciar Pessoa, o JPA resolve qual tabela buscar.
     *
     * Único por Pessoa: @JoinColumn(nullable = false, unique = true)
     * garante que não haja dois logins apontando para a mesma Pessoa.
     */
    @NotNull(message = "A pessoa associada é obrigatória")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;

    /**
     * Construtor útil para criar um novo usuário em código.
     */
    public Usuario(String username, String password, Pessoa pessoa) {
        this.username = username;
        this.password = password;
        this.pessoa = pessoa;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", pessoa=" + pessoa +
                '}';
    }
}

