package com.gestaoprojetos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "_cronograma")
public class Cronograma implements Serializable {

    //[] TODO: Colocar os Validações de cada campo

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatória")
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(length = 255, nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_cronograma", nullable = false)
    private StatusCronograma status = StatusCronograma.NAO_INICIADO;

    /**
     * Um Cronograma pode estar associado a vários Prêmios.
     * O lado “muitos” (Premio) deve ter:
     *   @ManyToOne
     *   @JoinColumn(name = "cronograma_id")
     *   private Cronograma cronograma;
     *
     * Aqui, Cascade.ALL fará com que salvar/remover um Cronograma
     * também persista ou remova os Prêmios associados (orphanRemoval = true
     * garante que, se você tirar um Prêmio da lista, ele seja excluído).
     */
    @OneToMany(mappedBy = "cronograma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Premio> premios = new ArrayList<>();

    public enum StatusCronograma {
        NAO_INICIADO,
        EM_ANDAMENTO,
        CONCLUIDO,
        ATRASADO,
        CANCELADO
    }

    public void setStatusById(Long id) {
        if (id == null) {
            this.status = null;
        } else {
            try {
                this.status = StatusCronograma.values()[(int) (id - 1)];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("ID de status inválido: " + id);
            }
        }
    }
}