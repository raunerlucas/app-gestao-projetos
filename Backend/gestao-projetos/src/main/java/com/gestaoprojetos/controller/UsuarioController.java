package com.gestaoprojetos.controller;

import com.gestaoprojetos.controller.DTO.UserResponseDTO;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.service.UsuarioServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;

    @Autowired
    public UsuarioController(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint to retrieve a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return ResponseEntity containing UserResponseDTO if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Retorna um usuário pelo ID",
            description = "Busca um usuário específico no sistema pelo seu ID. Retorna os detalhes do usuário se encontrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content())})
    public ResponseEntity<UserResponseDTO> getUserById(@RequestParam Long id) {
        try {
            Usuario user = usuarioService.buscarPorId(id);
            UserResponseDTO userResponse = new UserResponseDTO(user);
            return ResponseEntity.ok(userResponse);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
