package com.gestaoprojetos.controller;

import com.gestaoprojetos.controller.DTO.PessoaDTO;
import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Avaliador;
import com.gestaoprojetos.repository.AvaliadorRepository;
import com.gestaoprojetos.service.AvaliacaoServiceIMP;
import com.gestaoprojetos.service.AvaliadorServiceIMP;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/avaliadores")
public class AvaliadorController {
    private final AvaliadorServiceIMP avaliadorService;

    @Autowired
    public AvaliadorController(AvaliadorServiceIMP avaliadorService) {
        this.avaliadorService = avaliadorService;
    }

    /**
     * Endpoint para listar todos os avaliadores cadastrados no sistema.
     *
     * @return ResponseEntity com uma lista de avaliadores ou 204 No Content se não houver avaliadores.
     */
    // lista todos os avaliadores
    @GetMapping
    @Operation(summary = "Listar Avaliadores",
            description = "Retorna uma lista de avaliadores cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliadores obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PessoaDTO.PessoaResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum avaliador encontrado", content = @Content),
    })
    public ResponseEntity<List<PessoaDTO.PessoaResponseDTO>> lazyListarTodos() {
        List<PessoaDTO.PessoaResponseDTO> avaliadores = avaliadorService.listarTodos();
        if (avaliadores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(avaliadores);
    }

    /**
     * Endpoint para buscar um avaliador por ID.
     *
     * @param id ID do avaliador a ser buscado.
     * @return ResponseEntity com o avaliador encontrado ou 404 Not Found se não existir.
     */
    //Busca um avaliador por ID
    @GetMapping("/lazy/{id}")
    @Operation(summary = "Buscar Avaliador por ID",
            description = "Busca um Avaliador pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliador encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PessoaDTO.PessoaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Avaliador não encontrado", content = @Content),
    })
    public ResponseEntity<PessoaDTO.PessoaResponseDTO> LazyBuscarPorId(@PathVariable Long id) {
        try {
            PessoaDTO.PessoaResponseDTO avaliador = avaliadorService.LazyBuscarPorId(id);
            return ResponseEntity.ok(avaliador);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para criar um novo avaliador.
     *
     * @param avaliador Dados do avaliador a ser criado.
     * @return ResponseEntity com o avaliador criado ou 400 Bad Request se os dados forem inválidos.
     */
    // Criar um avaliador
    @PostMapping
    @Operation(summary = "Criar Avaliador",
            description = "Cria um novo avaliador com os dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliador criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PessoaDTO.PessoaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do avaliador.", content = @Content)})
    public ResponseEntity<?> criarAAvaliador(@RequestBody @Valid PessoaDTO.PessoaRequestDTO avaliador) {
        try {
            Avaliador novoAvaliador = avaliadorService.criarAvaliador(avaliador);
            return ResponseEntity.created(URI.create("/avaliadores/" + novoAvaliador.getId())).body(novoAvaliador);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Erro ao criar avaliador: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Avaliador",
            description = "Atualiza os dados de um avaliador existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliador atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliador.class))),
            @ApiResponse(responseCode = "404", description = "Avaliador não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização do avaliador", content = @Content)
    })
    public ResponseEntity<Avaliador> atualizarAvaliador(@PathVariable Long id, @RequestBody Avaliador avaliador) {
        Avaliador novoAvaliador = avaliadorService.atualizarAvaliador(id, avaliador);
        return ResponseEntity.ok(novoAvaliador);
    }
}