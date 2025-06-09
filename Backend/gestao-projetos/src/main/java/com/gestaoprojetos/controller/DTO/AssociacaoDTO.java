package com.gestaoprojetos.controller.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociacaoDTO {
    @NotNull(message = "ID da avaliação não pode ser nulo")
    private Long avaliacaoId;
}
