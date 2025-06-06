package com.gestaoprojetos.controller;

import com.gestaoprojetos.model.Avaliacao;
import com.gestaoprojetos.service.AutorServiceIMP;
import com.gestaoprojetos.service.AvaliacaoServiceIMP;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping
public class AvaliacaoController {
    private final AvaliacaoServiceIMP avaliacaoService;

    @Autowired
    public AvaliacaoController(AvaliacaoServiceIMP avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    @Operation(summary = "Criar Avaliação",
            description = "Cria uma nova avaliação para um projeto, associada a um autor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para a criação da avaliação", content = @Content),
    })
    public ResponseEntity<Avaliacao> criarAvaliacao(@RequestBody Avaliacao avaliacao) {
        Avaliacao novaAvaliacao = avaliacaoService.criarAvaliacao(avaliacao);
        return ResponseEntity.created(URI.create("/avaliacoes/" + novaAvaliacao.getId())).body(novaAvaliacao);
    }
}