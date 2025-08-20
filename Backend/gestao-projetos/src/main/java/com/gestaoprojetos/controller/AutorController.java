package com.gestaoprojetos.controller;

import com.gestaoprojetos.controller.DTO.AutorDTO;
import com.gestaoprojetos.controller.DTO.AutorDTO.AutorRequestDTO;
import com.gestaoprojetos.controller.DTO.AutorDTO.AutorResponseDTO;
import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.service.AutorServiceIMP;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller responsável por gerenciar operações relacionadas aos autores.
 * Implementa endpoints para CRUD completo e associações com projetos.
 *
 * @author Sistema de Gestão de Projetos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/autores")
@Tag(name = "Autores", description = "API para gerenciamento de autores do sistema")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AutorController {

    private final AutorServiceIMP autorService;

    @Autowired
    public AutorController(AutorServiceIMP autorService) {
        this.autorService = autorService;
    }

    /**
     * Lista todos os autores cadastrados no sistema.
     *
     * @return ResponseEntity com lista de autores ou 204 se vazio
     */
    @GetMapping
    @Operation(
        summary = "Listar todos os autores",
        description = "Retorna uma lista paginada de todos os autores cadastrados no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de autores obtida com sucesso",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AutorResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Nenhum autor encontrado no sistema",
            content = @Content
        )
    })
    public ResponseEntity<List<AutorResponseDTO>> listarTodos() {
        try {
            List<Autor> autores = autorService.listarTodos();

            if (autores.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<AutorResponseDTO> autoresDTO = autores.stream()
                    .map(AutorDTO::toAutorResponseDTO)
                    .toList();

            return ResponseEntity.ok(autoresDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca um autor específico pelo ID.
     *
     * @param id ID do autor a ser buscado
     * @return ResponseEntity com autor encontrado ou 404 se não existir
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar autor por ID",
        description = "Busca e retorna os dados de um autor específico pelo seu ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autor encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AutorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor não encontrado com o ID fornecido",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido fornecido",
            content = @Content
        )
    })
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID do autor", required = true, example = "1")
            @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body("ID deve ser um número positivo válido");
            }

            Autor autor = autorService.buscarPorId(id);
            AutorResponseDTO autorDTO = AutorDTO.toAutorResponseDTO(autor);
            return ResponseEntity.ok(autorDTO);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Cria um novo autor no sistema.
     *
     * @param autorRequestDTO Dados do autor a ser criado
     * @return ResponseEntity com autor criado ou erro de validação
     */
    @PostMapping
    @Operation(
        summary = "Criar novo autor",
        description = "Cria um novo autor no sistema com os dados fornecidos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Autor criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AutorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos para criação",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Autor já existe (conflito de dados)",
            content = @Content
        )
    })
    public ResponseEntity<?> criarAutor(
            @Parameter(description = "Dados do autor a ser criado", required = true)
            @RequestBody @Valid AutorRequestDTO autorRequestDTO) {
        try {
            if (autorRequestDTO == null) {
                return ResponseEntity.badRequest()
                    .body("Dados do autor são obrigatórios");
            }

            Autor autor = AutorDTO.toAutor(autorRequestDTO);
            Autor novoAutor = autorService.criarAutor(autor);
            AutorResponseDTO autorResponseDTO = AutorDTO.toAutorResponseDTO(novoAutor);

            return ResponseEntity.created(URI.create("/api/autores/" + novoAutor.getId()))
                    .body(autorResponseDTO);

        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                .body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Atualiza um autor existente.
     *
     * @param id ID do autor a ser atualizado
     * @param autorRequestDTO Dados atualizados do autor
     * @return ResponseEntity com autor atualizado ou erro
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar autor existente",
        description = "Atualiza os dados de um autor já cadastrado no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autor atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AutorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor não encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos para atualização",
            content = @Content
        )
    })
    public ResponseEntity<?> atualizarAutor(
            @Parameter(description = "ID do autor", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados do autor", required = true)
            @RequestBody @Valid AutorRequestDTO autorRequestDTO) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body("ID deve ser um número positivo válido");
            }

            if (autorRequestDTO == null) {
                return ResponseEntity.badRequest()
                    .body("Dados do autor são obrigatórios");
            }

            Autor autor = AutorDTO.toAutor(autorRequestDTO);
            Autor autorAtualizado = autorService.atualizarAutor(id, autor);
            AutorResponseDTO autorResponseDTO = AutorDTO.toAutorResponseDTO(autorAtualizado);

            return ResponseEntity.ok(autorResponseDTO);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                .body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Remove um autor do sistema.
     *
     * @param id ID do autor a ser removido
     * @return ResponseEntity com status 204 se removido com sucesso
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir autor",
        description = "Remove um autor do sistema. Esta operação é irreversível."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Autor excluído com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor não encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Não é possível excluir autor com projetos associados",
            content = @Content
        )
    })
    public ResponseEntity<?> excluirAutor(
            @Parameter(description = "ID do autor", required = true, example = "1")
            @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body("ID deve ser um número positivo válido");
            }

            autorService.deletarPorId(id);
            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Associa um projeto existente a um autor.
     *
     * @param autorId ID do autor
     * @param projetoId ID do projeto a ser associado
     * @return ResponseEntity com autor atualizado
     */
    @PostMapping("/{autorId}/projetos/{projetoId}")
    @Operation(
        summary = "Associar projeto ao autor",
        description = "Cria uma associação entre um autor e um projeto existente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Projeto associado ao autor com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AutorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor ou Projeto não encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Projeto já está associado ao autor",
            content = @Content
        )
    })
    public ResponseEntity<?> associarProjeto(
            @Parameter(description = "ID do autor", required = true, example = "1")
            @PathVariable Long autorId,
            @Parameter(description = "ID do projeto", required = true, example = "1")
            @PathVariable Long projetoId) {
        try {
            if (autorId == null || autorId <= 0 || projetoId == null || projetoId <= 0) {
                return ResponseEntity.badRequest()
                    .body("IDs devem ser números positivos válidos");
            }

            // Criar projeto temporário com ID para o service validar
            Projeto projeto = new Projeto();
            projeto.setId(projetoId);

            Autor autorAtualizado = autorService.adicionarProjeto(autorId, projeto);
            AutorResponseDTO autorResponseDTO = AutorDTO.toAutorResponseDTO(autorAtualizado);

            return ResponseEntity.ok(autorResponseDTO);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Remove a associação de um projeto com um autor.
     *
     * @param autorId ID do autor
     * @param projetoId ID do projeto a ser desassociado
     * @return ResponseEntity com autor atualizado
     */
    @DeleteMapping("/{autorId}/projetos/{projetoId}")
    @Operation(
        summary = "Remover associação projeto-autor",
        description = "Remove a associação entre um autor e um projeto."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Associação removida com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AutorResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor, Projeto ou associação não encontrada",
            content = @Content
        )
    })
    public ResponseEntity<?> removerAssociacao(
            @Parameter(description = "ID do autor", required = true, example = "1")
            @PathVariable Long autorId,
            @Parameter(description = "ID do projeto", required = true, example = "1")
            @PathVariable Long projetoId) {
        try {
            if (autorId == null || autorId <= 0 || projetoId == null || projetoId <= 0) {
                return ResponseEntity.badRequest()
                    .body("IDs devem ser números positivos válidos");
            }

            Autor autorAtualizado = autorService.removerProjeto(autorId, projetoId);
            AutorResponseDTO autorResponseDTO = AutorDTO.toAutorResponseDTO(autorAtualizado);

            return ResponseEntity.ok(autorResponseDTO);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Lista todos os projetos de um autor específico.
     *
     * @param autorId ID do autor
     * @return ResponseEntity com lista de projetos do autor
     */
    @GetMapping("/{autorId}/projetos")
    @Operation(
        summary = "Listar projetos do autor",
        description = "Retorna todos os projetos associados a um autor específico."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de projetos obtida com sucesso",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor não encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Autor não possui projetos associados",
            content = @Content
        )
    })
    public ResponseEntity<?> listarProjetosDoAutor(
            @Parameter(description = "ID do autor", required = true, example = "1")
            @PathVariable Long autorId) {
        try {
            if (autorId == null || autorId <= 0) {
                return ResponseEntity.badRequest()
                    .body("ID deve ser um número positivo válido");
            }

            List<Projeto> projetos = autorService.listarProjetos(autorId);

            if (projetos == null || projetos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<Long> projetosIds = projetos.stream()
                    .map(Projeto::getId)
                    .toList();

            return ResponseEntity.ok(projetosIds);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }
}
