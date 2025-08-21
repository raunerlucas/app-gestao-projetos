package com.gestaoprojetos.controller;

import com.gestaoprojetos.controller.DTO.CronogramaDTO;
import com.gestaoprojetos.controller.DTO.CronogramaDTO.CronogramaResponseDTO;
import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Cronograma;
import com.gestaoprojetos.model.Premio;
import com.gestaoprojetos.service.CronogramaServiceIMP;
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
@RequestMapping("/cronogramas")
public class CronogramaController {

    private final CronogramaServiceIMP cronogramaService;

    @Autowired
    public CronogramaController(CronogramaServiceIMP cronogramaService) {
        this.cronogramaService = cronogramaService;
    }

    /**
     * Controlador para gerenciar cronogramas no sistema.
     * Permite criar, listar, buscar por ID, atualizar e deletar cronogramas.
     * Também permite adicionar e remover prêmios de um cronograma específico.
     */
    //Criar Cronograma
    @PostMapping
    @Operation(summary = "Criar Cronograma", description = "Cria um novo cronograma.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cronograma criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cronograma.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação", content = @Content)
    })
    public ResponseEntity<?> criarCronograma(@RequestBody @Valid Cronograma cronograma) {
        try {
            Cronograma novo = cronogramaService.criarCronograma(cronograma);
            return ResponseEntity.created(URI.create("/cronogramas/" + novo.getId())).body(novo);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para listar todos os cronogramas cadastrados.
     * Retorna uma lista de cronogramas ou 204 se não houver nenhum.
     *  * @return ResponseEntity com a lista de cronogramas ou 204 se não houver nenhum.
     * */
    //Listar todos os cronogramas
    @GetMapping
    @Operation(summary = "Listar Cronogramas", description = "Retorna todos os cronogramas cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cronogramas obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CronogramaResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum cronograma encontrado", content = @Content)
    })
    public ResponseEntity<List<CronogramaResponseDTO>> listarCronogramas() {
        List<CronogramaResponseDTO> lista = cronogramaService.listarTodos()
                .stream()
                .map(CronogramaDTO::toCronogramaResponseDTO)
                .toList();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint para buscar um cronograma por ID.
     * Retorna o cronograma encontrado ou 404 se não existir.
     *
     * @param id ID do cronograma a ser buscado.
     * @return ResponseEntity com o cronograma encontrado ou 404 se não existir.
     */
    // Buscar cronograma por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar Cronograma por ID", description = "Retorna os dados de um cronograma pelo ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cronograma encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CronogramaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cronograma não encontrado", content = @Content)
    })
    public ResponseEntity<?> buscarCronogramaPorId(@PathVariable Long id) {
        try {
            CronogramaResponseDTO cronograma = CronogramaDTO.toCronogramaResponseDTO(cronogramaService.buscarPorId(id));
            if (cronograma == null) {
                return ResponseEntity.status(404).body("Cronograma não encontrado");
            }
            return ResponseEntity.ok(cronograma);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Controlador para gerenciar cronogramas no sistema.
     * Permite criar, listar, buscar por ID, atualizar e deletar cronogramas.
     * Também permite adicionar e remover prêmios de um cronograma específico.
     */
    // Atualizar cronograma
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Cronograma", description = "Atualiza os dados de um cronograma.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cronograma atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cronograma.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cronograma não encontrado", content = @Content)
    })
    public ResponseEntity<?> atualizarCronograma(@PathVariable Long id, @RequestBody @Valid Cronograma cronograma) {
        try {
            Cronograma atualizado = cronogramaService.atualizarCronograma(id, cronograma);
            return ResponseEntity.ok(atualizado);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Endpoint para deletar um cronograma pelo ID.
     * Retorna 204 se deletado com sucesso ou 404 se não existir.
     *
     * @param id ID do cronograma a ser deletado.
     * @return ResponseEntity com status 204 ou mensagem de erro 404.
     */
    //Deletar cronograma
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Cronograma", description = "Remove um cronograma pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cronograma removido com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cronograma não encontrado", content = @Content)
    })
    public ResponseEntity<?> deletarCronograma(@PathVariable Long id) {
        try {
            cronogramaService.deletarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Endpoint para listar prêmios vinculados a um cronograma específico.
     * Retorna a lista de prêmios ou 404 se o cronograma não existir.
     *
     * @param cronogramaId ID do cronograma cujos prêmios serão listados.
     * @return ResponseEntity com a lista de prêmios ou 404 se o cronograma não existir.
     */
    //Adicionar Prêmio ao Cronograma
    @PostMapping("/{cronogramaId}/premios")
    @Operation(summary = "Adicionar Prêmio ao Cronograma", description = "Adiciona um prêmio à lista de um cronograma.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prêmio adicionado ao cronograma com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cronograma.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para adição", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cronograma não encontrado", content = @Content)
    })
    public ResponseEntity<?> adicionarPremio(@PathVariable Long cronogramaId, @RequestBody @Valid Premio premio) {
        try {
            Cronograma atualizado = cronogramaService.adicionarPremio(cronogramaId, premio);
            return ResponseEntity.ok(atualizado);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Endpoint para remover um prêmio de um cronograma específico.
     * Retorna o cronograma atualizado ou 404 se o cronograma ou prêmio não existir.
     *
     * @param cronogramaId ID do cronograma do qual o prêmio será removido.
     * @param premioId ID do prêmio a ser removido.
     * @return ResponseEntity com o cronograma atualizado ou mensagem de erro 404.
     */
    //Remover Prêmio do Cronograma
    @DeleteMapping("/{cronogramaId}/premios/{premioId}")
    @Operation(summary = "Remover Prêmio do Cronograma", description = "Remove um prêmio do cronograma.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prêmio removido do cronograma com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cronograma.class))),
            @ApiResponse(responseCode = "404", description = "Cronograma ou prêmio não encontrado", content = @Content)
    })
    public ResponseEntity<?> removerPremio(@PathVariable Long cronogramaId, @PathVariable Long premioId) {
        try {
            Cronograma atualizado = cronogramaService.removerPremio(cronogramaId, premioId);
            return ResponseEntity.ok(atualizado);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
