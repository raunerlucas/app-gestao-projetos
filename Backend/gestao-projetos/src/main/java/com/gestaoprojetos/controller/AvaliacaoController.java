package com.gestaoprojetos.controller;

import com.gestaoprojetos.exception.ResourceNotFoundException;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
public class AvaliacaoController {
    private final AvaliacaoServiceIMP avaliacaoService;

    @Autowired
    public AvaliacaoController(AvaliacaoServiceIMP avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    /**
     * Endpoint para criar uma nova avaliação.
     *
     * @param avaliacao Objeto Avaliacao a ser criado.
     * @return ResponseEntity com a avaliação criada e status 201 (Created).
     */
    //Criar avaliação
    @PostMapping("/avaliacoes")
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

    /**
     * Endpoint para obter uma avaliação por ID.
     *
     * @return ResponseEntity com a avaliação encontrada ou 404 se não existir.
     */
    //Lista de avaliações
    @GetMapping("/avaliacoes")
    @Operation(summary = "Listar Avaliações",
            description = "Retorna todas as avaliações cadastradas no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class))),
            @ApiResponse(responseCode = "204", description = "Nenhuma avaliação encontrada", content = @Content),
    })
    public ResponseEntity<List<Avaliacao>> listarAvaliacoes() {
        List<Avaliacao> avaliacoes = avaliacaoService.listarTodos();
        if (avaliacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(avaliacoes);
    }

    /**
     * Endpoint para atualizar uma avaliação existente.
     *
     * @param id ID da avaliação a ser atualizada.
     * @return ResponseEntity com a avaliação atualizada ou 404 se não existir.
     */
    //buscar avaliação por ID
    @GetMapping("/avaliacoes/{id}")
    @Operation(summary = "Buscar Avaliação por ID",
            description = "Busca uma avaliação pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content),
    })
    public ResponseEntity<Avaliacao> buscarAvaliacaoPorId(@PathVariable Long id) {
        try {
            Avaliacao avaliacao = avaliacaoService.buscarPorId(id);
            return ResponseEntity.ok(avaliacao);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para atualizar uma avaliação existente.
     *
     * @param avaliadorId ID da avaliação a ser atualizada.
     * @return ResponseEntity com a avaliação atualizada ou 404 se não existir.
     */
    // Buscar avaliações por avaliador
    @GetMapping("/avaliador/{avaliadorId}")
    @Operation(summary = "Listar Avaliações por Avaliador", description = "Lista todas as avaliações feitas por um avaliador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações do avaliador encontrada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Avaliador não encontrado", content = @Content),
    })
    public ResponseEntity<List<Avaliacao>> listarPorAvaliador(@PathVariable Long avaliadorId) {
        try {
            List<Avaliacao> lista = avaliacaoService.listarPorAvaliador(avaliadorId);
            if (lista.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(lista);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para listar avaliações por projeto.
     *
     * @param projetoId ID do projeto cujas avaliações serão listadas.
     * @return ResponseEntity com a lista de avaliações do projeto ou 404 se o projeto não existir.
     */
    // Listar avaliações por projeto
    @GetMapping("/projeto/{projetoId}")
    @Operation(summary = "Listar Avaliações por Projeto", description = "Retorna todas as avaliações de um projeto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações do projeto obtida com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content),
    })
    public ResponseEntity<List<Avaliacao>> listarPorProjeto(@PathVariable Long projetoId) {
        try {
            List<Avaliacao> lista = avaliacaoService.listarPorProjeto(projetoId);
            if (lista.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(lista);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}