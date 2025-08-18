package com.gestaoprojetos.controller;

import com.gestaoprojetos.controller.DTO.PremioDTO;
import com.gestaoprojetos.controller.DTO.PremioDTO.PremioResponseDTO;
import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Premio;
import com.gestaoprojetos.service.PremioServiceIMP;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/premios")
public class PremioController {

    private final PremioServiceIMP premioService;

    @Autowired
    public PremioController(PremioServiceIMP premioService) {
        this.premioService = premioService;
    }

    /**
     * Controlador para gerenciar prêmios no sistema.
     * Permite criar, listar, buscar por ID, atualizar e deletar prêmios.
     * Também permite listar prêmios vinculados a um cronograma específico.
     */
    //Criar Prêmio
    @PostMapping
    @Operation(summary = "Criar Prêmio", description = "Cria um novo prêmio vinculado a um cronograma existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prêmio criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Premio.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do prêmio", content = @Content)
    })
    public ResponseEntity<?> criarPremio(@RequestBody @Valid Premio premio) {
        try {
            Premio novoPremio = premioService.criarPremio(premio);
            return ResponseEntity.created(URI.create("/premios/" + novoPremio.getId())).body(novoPremio);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Erro ao criar prêmio: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /*  Endpoint para listar todos os prêmios cadastrados.
     * Retorna uma lista de prêmios ou 204 se não houver nenhum.
     */
    //Listar todos os prêmios
    @GetMapping
    @Operation(summary = "Listar Prêmios", description = "Retorna todos os prêmios cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de prêmios obtida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PremioResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum prêmio encontrado", content = @Content)
    })
    public ResponseEntity<List<PremioResponseDTO>> listarPremios() {
        List<PremioResponseDTO> premios = premioService.listarTodos().stream().map(premio -> new PremioDTO().toPremioResponseDTO(premio)).toList();
        if (premios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(premios);
    }


    /**
     * Endpoint para buscar um prêmio por ID.
     * Retorna o prêmio correspondente ou 404 se não existir.
     */
    // Buscar prêmio por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar Prêmio por ID", description = "Retorna os dados de um prêmio pelo ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prêmio encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Premio.class))),
            @ApiResponse(responseCode = "404", description = "Prêmio não encontrado", content = @Content)
    })
    public ResponseEntity<?> buscarPremioPorId(@PathVariable Long id) {
        try {
            Premio premio = premioService.buscarPorId(id);
            return ResponseEntity.ok(premio);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Endpoint para atualizar um prêmio existente.
     * Recebe os novos dados e atualiza o prêmio correspondente.
     * Retorna o prêmio atualizado ou 404 se não existir.
     */
    //Atualizar prêmio
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Prêmio", description = "Atualiza os dados de um prêmio já cadastrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prêmio atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Premio.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização", content = @Content),
            @ApiResponse(responseCode = "404", description = "Prêmio não encontrado", content = @Content)
    })
    public ResponseEntity<?> atualizarPremio(@PathVariable Long id, @RequestBody @Valid Premio premio) {
        try {
            Premio atualizado = premioService.atualizarPremio(id, premio);
            return ResponseEntity.ok(atualizado);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar prêmio: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Endpoint para deletar um prêmio pelo ID.
     * Retorna 204 se removido com sucesso ou 404 se não existir.
     */
    //Deletar prêmio
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Prêmio", description = "Remove um prêmio pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prêmio removido com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Prêmio não encontrado", content = @Content)
    })
    public ResponseEntity<?> deletarPremio(@PathVariable Long id) {
        try {
            premioService.deletarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Endpoint para listar prêmios vinculados a um cronograma específico.
     * @param cronogramaId
     * @return
     */
    //Listar prêmios por cronograma
    @GetMapping("/cronograma/{cronogramaId}")
    @Operation(summary = "Listar Prêmios por Cronograma", description = "Retorna todos os prêmios vinculados a um cronograma específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de prêmios do cronograma obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Premio.class))),
            @ApiResponse(responseCode = "404", description = "Cronograma não encontrado", content = @Content),
            @ApiResponse(responseCode = "204", description = "Nenhum prêmio vinculado ao cronograma", content = @Content)
    })
    public ResponseEntity<?> listarPremiosPorCronograma(@PathVariable Long cronogramaId) {
        try {
            List<Premio> premios = premioService.listarPorCronograma(cronogramaId);
            if (premios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(premios);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
