package com.gestaoprojetos.controller;

import com.gestaoprojetos.controller.DTO.ProjetoDTO;
import com.gestaoprojetos.controller.DTO.ProjetoDTO.ProjetoRequestDTO;
import com.gestaoprojetos.controller.DTO.ProjetoDTO.ProjetoResponseDTO;
import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Avaliacao;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.service.ProjetoServiceIMP;
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
 * Controller responsável por gerenciar operações relacionadas aos projetos.
 * Implementa endpoints para CRUD completo e gestão de autores e avaliações.
 *
 * @author Sistema de Gestão de Projetos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projetos")
@Tag(name = "Projetos", description = "API para gerenciamento de projetos do sistema")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjetoController {

    private final ProjetoServiceIMP projetoService;

    @Autowired
    public ProjetoController(ProjetoServiceIMP projetoService) {
        this.projetoService = projetoService;
    }

    /**
     * Lista todos os projetos cadastrados no sistema.
     *
     * @return ResponseEntity com lista de projetos ou 204 se vazio
     */
    @GetMapping
    @Operation(
        summary = "Listar todos os projetos",
        description = "Retorna uma lista completa de todos os projetos cadastrados no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de projetos obtida com sucesso",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProjetoResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Nenhum projeto encontrado no sistema",
            content = @Content
        )
    })
    public ResponseEntity<List<ProjetoResponseDTO>> listarTodos() {
        try {
            List<Projeto> projetos = projetoService.listarTodos();

            if (projetos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<ProjetoResponseDTO> projetosDTO = projetos.stream()
                    .map(ProjetoDTO::toProjetoResponseDTO)
                    .toList();

            return ResponseEntity.ok(projetosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca um projeto específico pelo ID.
     *
     * @param id ID do projeto a ser buscado
     * @return ResponseEntity com projeto encontrado ou 404 se não existir
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar projeto por ID",
        description = "Busca e retorna os dados de um projeto específico pelo seu ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Projeto encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto não encontrado com o ID fornecido",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido fornecido",
            content = @Content
        )
    })
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID do projeto", required = true, example = "1")
            @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body("ID deve ser um número positivo válido");
            }

            Projeto projeto = projetoService.buscarPorId(id);
            ProjetoResponseDTO projetoDTO = ProjetoDTO.toProjetoResponseDTO(projeto);
            return ResponseEntity.ok(projetoDTO);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Cria um novo projeto no sistema.
     *
     * @param projetoRequestDTO Dados do projeto a ser criado
     * @return ResponseEntity com projeto criado ou erro de validação
     */
    @PostMapping
    @Operation(
        summary = "Criar novo projeto",
        description = "Cria um novo projeto no sistema com os dados fornecidos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Projeto criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos para criação",
            content = @Content
        )
    })
    public ResponseEntity<?> criarProjeto(
            @Parameter(description = "Dados do projeto a ser criado", required = true)
            @RequestBody @Valid ProjetoRequestDTO projetoRequestDTO) {
        try {
            if (projetoRequestDTO == null) {
                return ResponseEntity.badRequest()
                    .body("Dados do projeto são obrigatórios");
            }

            Projeto novoProjeto = projetoService.criarProjeto(projetoRequestDTO);
            ProjetoResponseDTO projetoResponseDTO = ProjetoDTO.toProjetoResponseDTO(novoProjeto);

            return ResponseEntity.created(URI.create("/api/projetos/" + novoProjeto.getId()))
                    .body(projetoResponseDTO);

        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                .body("Erro de validação: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest()
                .body("Erro de referência: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Atualiza um projeto existente.
     *
     * @param id ID do projeto a ser atualizado
     * @param projetoRequestDTO Dados atualizados do projeto
     * @return ResponseEntity com projeto atualizado ou erro
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar projeto existente",
        description = "Atualiza os dados de um projeto já cadastrado no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Projeto atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto não encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos para atualização",
            content = @Content
        )
    })
    public ResponseEntity<?> atualizarProjeto(
            @Parameter(description = "ID do projeto", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados do projeto", required = true)
            @RequestBody @Valid ProjetoRequestDTO projetoRequestDTO) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body("ID deve ser um número positivo válido");
            }

            if (projetoRequestDTO == null) {
                return ResponseEntity.badRequest()
                    .body("Dados do projeto são obrigatórios");
            }

            Projeto projetoAtualizado = projetoService.atualizarProjeto(id, projetoRequestDTO);
            ProjetoResponseDTO projetoResponseDTO = ProjetoDTO.toProjetoResponseDTO(projetoAtualizado);

            return ResponseEntity.ok(projetoResponseDTO);

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
     * Remove um projeto do sistema.
     *
     * @param id ID do projeto a ser removido
     * @return ResponseEntity com status 204 se removido com sucesso
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir projeto",
        description = "Remove um projeto do sistema. Esta operação é irreversível."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Projeto excluído com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto não encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Não é possível excluir projeto com avalia��ões",
            content = @Content
        )
    })
    public ResponseEntity<?> excluirProjeto(
            @Parameter(description = "ID do projeto", required = true, example = "1")
            @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body("ID deve ser um número positivo válido");
            }

            projetoService.deletarPorId(id);
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
     * Lista projetos sem avaliações.
     *
     * @return ResponseEntity com lista de projetos sem avaliações
     */
    @GetMapping("/sem-avaliacoes")
    @Operation(
        summary = "Listar projetos sem avaliações",
        description = "Retorna todos os projetos que ainda não possuem avaliações."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de projetos sem avaliações obtida com sucesso",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProjetoResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Todos os projetos possuem avaliações",
            content = @Content
        )
    })
    public ResponseEntity<List<ProjetoResponseDTO>> listarProjetosSemAvaliacao() {
        try {
            List<Projeto> projetos = projetoService.listarProjetosSemAvaliacao();

            if (projetos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<ProjetoResponseDTO> projetosDTO = projetos.stream()
                    .map(ProjetoDTO::toProjetoResponseDTO)
                    .toList();

            return ResponseEntity.ok(projetosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista projetos com avaliações.
     *
     * @return ResponseEntity com lista de projetos com avaliações
     */
    @GetMapping("/com-avaliacoes")
    @Operation(
        summary = "Listar projetos com avaliações",
        description = "Retorna todos os projetos que já possuem pelo menos uma avaliação."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de projetos com avaliações obtida com sucesso",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProjetoResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Nenhum projeto possui avaliações",
            content = @Content
        )
    })
    public ResponseEntity<List<ProjetoResponseDTO>> listarProjetosComAvaliacao() {
        try {
            List<Projeto> projetos = projetoService.listarProjetosComAvaliacao();

            if (projetos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<ProjetoResponseDTO> projetosDTO = projetos.stream()
                    .map(ProjetoDTO::toProjetoResponseDTO)
                    .toList();

            return ResponseEntity.ok(projetosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista projetos vencedores ordenados por nota.
     *
     * @return ResponseEntity com lista de projetos vencedores
     */
    @GetMapping("/vencedores")
    @Operation(
        summary = "Listar projetos vencedores",
        description = "Retorna projetos vencedores ordenados por nota em ordem decrescente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de projetos vencedores obtida com sucesso",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProjetoResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Nenhum projeto vencedor encontrado",
            content = @Content
        )
    })
    public ResponseEntity<List<ProjetoResponseDTO>> listarProjetosVencedores() {
        try {
            List<Projeto> projetos = projetoService.listarProjetosVencedoresPorNotaDesc();

            if (projetos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<ProjetoResponseDTO> projetosDTO = projetos.stream()
                    .map(ProjetoDTO::toProjetoResponseDTO)
                    .toList();

            return ResponseEntity.ok(projetosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Adiciona um autor existente a um projeto.
     *
     * @param projetoId ID do projeto
     * @param autorId ID do autor a ser adicionado
     * @return ResponseEntity com projeto atualizado
     */
    @PostMapping("/{projetoId}/autores/{autorId}")
    @Operation(
        summary = "Adicionar autor ao projeto",
        description = "Associa um autor existente a um projeto."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autor adicionado ao projeto com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto ou Autor não encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Autor já está associado ao projeto",
            content = @Content
        )
    })
    public ResponseEntity<?> adicionarAutor(
            @Parameter(description = "ID do projeto", required = true, example = "1")
            @PathVariable Long projetoId,
            @Parameter(description = "ID do autor", required = true, example = "1")
            @PathVariable Long autorId) {
        try {
            if (projetoId == null || projetoId <= 0 || autorId == null || autorId <= 0) {
                return ResponseEntity.badRequest()
                    .body("IDs devem ser números positivos válidos");
            }

            // Criar autor temporário com ID para o service validar
            Autor autor = new Autor();
            autor.setId(autorId);

            Projeto projetoAtualizado = projetoService.adicionarAutor(projetoId, autor);
            ProjetoResponseDTO projetoResponseDTO = ProjetoDTO.toProjetoResponseDTO(projetoAtualizado);

            return ResponseEntity.ok(projetoResponseDTO);

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
     * Remove um autor de um projeto.
     *
     * @param projetoId ID do projeto
     * @param autorId ID do autor a ser removido
     * @return ResponseEntity com projeto atualizado
     */
    @DeleteMapping("/{projetoId}/autores/{autorId}")
    @Operation(
        summary = "Remover autor do projeto",
        description = "Remove a associação entre um autor e um projeto."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autor removido do projeto com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto, Autor ou associação não encontrada",
            content = @Content
        )
    })
    public ResponseEntity<?> removerAutor(
            @Parameter(description = "ID do projeto", required = true, example = "1")
            @PathVariable Long projetoId,
            @Parameter(description = "ID do autor", required = true, example = "1")
            @PathVariable Long autorId) {
        try {
            if (projetoId == null || projetoId <= 0 || autorId == null || autorId <= 0) {
                return ResponseEntity.badRequest()
                    .body("IDs devem ser números positivos válidos");
            }

            Projeto projetoAtualizado = projetoService.removerAutor(projetoId, autorId);
            ProjetoResponseDTO projetoResponseDTO = ProjetoDTO.toProjetoResponseDTO(projetoAtualizado);

            return ResponseEntity.ok(projetoResponseDTO);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Adiciona uma avaliação a um projeto.
     *
     * @param projetoId ID do projeto
     * @param avaliacaoId ID da avaliação a ser adicionada
     * @return ResponseEntity com projeto atualizado
     */
    @PostMapping("/{projetoId}/avaliacoes/{avaliacaoId}")
    @Operation(
        summary = "Adicionar avaliação ao projeto",
        description = "Associa uma avaliação existente a um projeto."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Avaliação adicionada ao projeto com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto ou Avaliação não encontrada",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Avaliação já está associada ao projeto",
            content = @Content
        )
    })
    public ResponseEntity<?> adicionarAvaliacao(
            @Parameter(description = "ID do projeto", required = true, example = "1")
            @PathVariable Long projetoId,
            @Parameter(description = "ID da avaliação", required = true, example = "1")
            @PathVariable Long avaliacaoId) {
        try {
            if (projetoId == null || projetoId <= 0 || avaliacaoId == null || avaliacaoId <= 0) {
                return ResponseEntity.badRequest()
                    .body("IDs devem ser números positivos válidos");
            }

            // Criar avaliação temporária com ID para o service validar
            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setId(avaliacaoId);

            Projeto projetoAtualizado = projetoService.adicionarAvaliacao(projetoId, avaliacao);
            ProjetoResponseDTO projetoResponseDTO = ProjetoDTO.toProjetoResponseDTO(projetoAtualizado);

            return ResponseEntity.ok(projetoResponseDTO);

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
     * Remove uma avaliação de um projeto.
     *
     * @param projetoId ID do projeto
     * @param avaliacaoId ID da avaliação a ser removida
     * @return ResponseEntity com projeto atualizado
     */
    @DeleteMapping("/{projetoId}/avaliacoes/{avaliacaoId}")
    @Operation(
        summary = "Remover avaliação do projeto",
        description = "Remove a associação entre uma avaliação e um projeto."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Avaliação removida do projeto com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjetoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto, Avaliação ou associação não encontrada",
            content = @Content
        )
    })
    public ResponseEntity<?> removerAvaliacao(
            @Parameter(description = "ID do projeto", required = true, example = "1")
            @PathVariable Long projetoId,
            @Parameter(description = "ID da avaliação", required = true, example = "1")
            @PathVariable Long avaliacaoId) {
        try {
            if (projetoId == null || projetoId <= 0 || avaliacaoId == null || avaliacaoId <= 0) {
                return ResponseEntity.badRequest()
                    .body("IDs devem ser números positivos válidos");
            }

            Projeto projetoAtualizado = projetoService.removerAvaliacao(projetoId, avaliacaoId);
            ProjetoResponseDTO projetoResponseDTO = ProjetoDTO.toProjetoResponseDTO(projetoAtualizado);

            return ResponseEntity.ok(projetoResponseDTO);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }
}
