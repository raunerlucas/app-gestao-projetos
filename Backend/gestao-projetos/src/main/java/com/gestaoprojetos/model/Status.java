package com.gestaoprojetos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity(name = "_status")
@NoArgsConstructor
@AllArgsConstructor
public class Status implements Serializable {

    @Id
    private Long id;

    private String description;

    public enum Values {
        PENDENTE(1L, "Pendente"),
        CONCLUIDO(2L, "Avaliado"),
        AVALIANDO(3L, "Em Avaliação"),
        CANCELADO(4L, "Cancelado");

        private final Long id;
        private final String description;

        Values(Long id, String description) {
            this.id = id;
            this.description = description;
        }

        public Status toStatus() {
            return new Status(id, description);
        }
    }
}