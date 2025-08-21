package com.gestaoprojetos.controller;


import com.gestaoprojetos.controller.DTO.UsuarioDTO;
import com.gestaoprojetos.controller.DTO.UsuarioDTO.UsuarioResponseDTO;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Pessoa;
import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.service.UsuarioServiceIMP;
import com.gestaoprojetos.utils.Tools;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.gestaoprojetos.controller.DTO.UsuarioDTO.UsuarioRequestDTO;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioServiceIMP usuarioService;

    @Autowired
    public UsuarioController(UsuarioServiceIMP usuarioService) {
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
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content())})
    public ResponseEntity<UsuarioResponseDTO> getUserById(@RequestParam Long id) {
        try {
            Usuario user = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(UsuarioDTO.toUsuarioResponseDTO(user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Endpoint to create a new user.
     *
     * @param userReq the request body containing user details
     * @return ResponseEntity containing the created UserResponseDTO
     */
    @PostMapping
    @Operation(summary = "Cria um novo usuário",
            description = "Registra um novo usuário no sistema com os detalhes fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content())})
    public ResponseEntity<UsuarioResponseDTO> setNewUser(@RequestBody @Valid UsuarioRequestDTO userReq) {
        Usuario usuario = new Usuario();
        usuario.setUsername(userReq.getUsername());
        usuario.setPassword(userReq.getPassword());
        Pessoa pessoa = Tools.findPessoaById(userReq.getPessoaId());
        if (pessoa == null) {
            return ResponseEntity.badRequest().body(null);
        }
        usuario.setPessoa(pessoa);


        Usuario savedUser = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(UsuarioDTO.toUsuarioResponseDTO(savedUser));
    }
}
