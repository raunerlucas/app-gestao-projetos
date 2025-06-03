package com.gestaoprojetos.controller;

import com.gestaoprojetos.model.Status;
import com.gestaoprojetos.service.StatusServiceIMP;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusController {


    private final StatusServiceIMP statusService;

    @Autowired
    public StatusController(StatusServiceIMP statusService) {
        this.statusService = statusService;
    }

    /**
     * Endpoint para obter todos os Status disponíveis.
     * @return ResposenEntity com uma Lista de Status.
     */
    @GetMapping
    @Operation(summary = "Listar Status",
            description = "Retorna todos os Status disponíveis no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Status obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Status.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum Status encontrado", content = @Content),
    })
    public ResponseEntity<List<Status>> listarTodos() {
        List<Status> statusList = statusService.findAll();
        if (statusList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(statusList);
    }
}
