package com.gestaoprojetos.controller.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AutorLazyDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;

}
