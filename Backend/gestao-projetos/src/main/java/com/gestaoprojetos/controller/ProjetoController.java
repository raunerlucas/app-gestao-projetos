package com.gestaoprojetos.controller;

import com.gestaoprojetos.controller.DTO.*;
import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Avaliacao;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.service.ProjetoServiceIMP;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import java.util.List;

@RestController
@RequestMapping("/projetos")

public class ProjetoController {
    private final ProjetoServiceIMP projetoService;

    @Autowired
    public ProjetoController(ProjetoServiceIMP projetoService) {
        this.projetoService = projetoService;
    }

    /**
     * Endpoint para listar todos os projetos cadastrados no sistema (lazy).
     *
     * @return ResponseEntity com uma lista de projetos ou 204 No Content se não houver projetos.
     */
    //Lista todos com (lazy)
    @GetMapping("/lazy")
    @Operation(summary = "Listar Projetos",
            description = "Retorna uma lista de projetos cadastrados no sistema (dados resumidos).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de projetos obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjetoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum projeto encontrado", content = @Content),
    })
    public ResponseEntity<List<ProjetoResponseDTO>> lazyListarTodos() {
        List<ProjetoResponseDTO> projetos = projetoService.listarTodos().stream()
                .map(ProjetoServiceIMP::toProjetoResponseDTO)
                .toList();
        if (projetos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(projetos);
    }

    /**
     * Endpoint para buscar um projeto por ID (lazy)..
     *
     * @return ResponseEntity com o projeto criado e o status 201 Created.
     */
    //Buscar um projeto por ID
    @GetMapping("/lazy/{id}")
    @Operation(summary = "Buscar Projeto por ID",
            description = "Busca um projeto pelo ID fornecido (dados resumidos).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjetoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content),
    })
    public ResponseEntity<ProjetoResponseDTO> LazyBuscarPorId(@PathVariable Long id) {
        try {
            Projeto projeto = projetoService.buscarPorId(id);
            ProjetoResponseDTO dto = ProjetoServiceIMP.toProjetoResponseDTO(projeto);
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // EXCLUIR projeto
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Projeto", description = "Exclui um projeto pelo ID.")
    public ResponseEntity<?> excluirProjeto(@PathVariable Long id) {
        try {
            projetoService.deletarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Endpoint para criar um novo projeto.
     *
     * @param dto Dados do projeto a ser criado.
     * @return ResponseEntity com o projeto criado e o status 201 Created.
     */
    // Cria um novo projeto
    @PostMapping
    @Operation(summary = "Criar Projeto",
            description = "Cria um novo projeto com os dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjetoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do projeto.", content = @Content)})
    public ResponseEntity<?> criarProjeto(@RequestBody @Valid ProjetoRequestDTO dto) {
        try {
            Projeto novoProjeto = projetoService.criarProjeto(dto);
            ProjetoResponseDTO response = ProjetoServiceIMP.toProjetoResponseDTO(novoProjeto);
            return ResponseEntity.created(URI.create("/projetos/" + novoProjeto.getId())).body(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body("Erro ao criar projeto: " + e.getMessage());
        }
    }

    /**
     * Endpoint para atualizar um projeto existente.
     *
     * @param id  ID do projeto a ser atualizado.
     * @param dto Dados atualizados do projeto.
     * @return ResponseEntity com o projeto atualizado ou erro se não encontrado.
     */
    // Atualiza um projeto existente
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Projeto", description = "Atualiza um projeto existente pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjetoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização do projeto.", content = @Content)
    })
    public ResponseEntity<?> atualizarProjeto(@PathVariable Long id, @RequestBody @Valid ProjetoRequestDTO dto) {
        try {
            Projeto atualizado = projetoService.atualizarProjeto(id, dto);
            ProjetoResponseDTO response = ProjetoServiceIMP.toProjetoResponseDTO(atualizado);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado: " + e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar projeto: " + e.getMessage());
        }
    }

    /**
     * Endpoint para listar todos os projetos cadastrados no sistema (eager) SEM AVALIAÇOES.
     *
     * @return ResponseEntity com uma lista de projetos ou 204 No Content se não houver projetos.
     */
    //LISTAR PROJETOS SEM AVALIAÇÕES[
    @GetMapping("/sem-avaliacoes")
    @Operation(summary = "Listar Projetos sem Avaliações",
            description = "Retorna uma lista de projetos que ainda não possuem avaliações.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de projetos sem avaliações obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjetoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum projeto sem avaliações encontrado", content = @Content),
    })
    public ResponseEntity<List<ProjetoResponseDTO>> listarSemAvaliacao() {
        var lista = projetoService.listarProjetosSemAvaliacao().stream()
                .map(ProjetoServiceIMP::toProjetoResponseDTO)
                .toList();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint para listar todos os projetos cadastrados no sistema (eager) COM AVALIAÇÕES.
     *
     * @return ResponseEntity com uma lista de projetos ou 204 No Content se não houver projetos.
     */
    //LISTAR PROJETOS COM AVALIAÇÕES
    @GetMapping("/com-avaliacoes")
    @Operation(summary = "Listar Projetos com Avaliações",
            description = "Retorna uma lista de projetos que possuem avaliações.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de projetos com avaliações obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjetoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum projeto com avaliações encontrado", content = @Content),
    })
    public ResponseEntity<List<ProjetoResponseDTO>> listarComAvaliacao() {
        var lista = projetoService.listarProjetosComAvaliacao().stream()
                .map(ProjetoServiceIMP::toProjetoResponseDTO)
                .toList();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint para listar projetos vencedores por nota em ordem decrescente.
     *
     * @return ResponseEntity com uma lista de projetos vencedores ou 204 No Content se não houver projetos.
     * @throws ResourceNotFoundException se o projeto não for encontrado.
     */
    // LISTAR PROJETOS VENCEDORES
    @GetMapping("/vencedores")
    @Operation(summary = "Listar Projetos Vencedores",
            description = "Retorna uma lista de projetos vencedores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de projetos vencedores obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjetoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum projeto vencedor encontrado", content = @Content),
    })
    public ResponseEntity<List<ProjetoResponseDTO>> listarVencedores() {
        var lista = projetoService.listarProjetosVencedoresPorNotaDesc().stream()
                .map(ProjetoServiceIMP::toProjetoResponseDTO)
                .toList();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint para adcionar projetos autor ao projeto
     *
     * @param projetoId ID do projeto ao qual o autor será adicionado.
     * @param dto       Dados do autor a ser adicionado.
     * @return ResponseEntity com o projeto atualizado ou erro se não encontrado.
     * @throws ResourceNotFoundException se o projeto ou autor não for encontrado.
     * @throws BadRequestException       se houver erro ao adicionar o autor.
     */
    // ADICIONAR AUTOR AO PROJETO
    @PostMapping("/{id}/autores/{idAutor}")
    @Operation(summary = "Adicionar Autor ao Projeto",
            description = "Adiciona um autor ao projeto pelo ID do projeto e do autor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor adicionado ao projeto com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjetoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Projeto ou Autor não encontrado", content = @Content),
    })
    public ResponseEntity<?> adicionarAutor(@PathVariable Long projetoId, @RequestBody @Valid AutorResumoDTO dto) {
        try {
            Autor autor = new Autor(dto.getId(), dto.getNome(), null, null, null, null); // ajuste se necessário
            Projeto atualizado = projetoService.adicionarAutor(projetoId, autor);
            ProjetoResponseDTO response = ProjetoServiceIMP.toProjetoResponseDTO(atualizado);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException | BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para remover um autor de um projeto.
     *
     * @param projetoId ID do projeto do qual o autor será removido.
     * @param autorId   ID do autor a ser removido.
     * @return ResponseEntity com o projeto atualizado ou erro se não encontrado.
     * @throws ResourceNotFoundException se o projeto ou autor não for encontrado.
     */
    // ENDPOINTS PARA REMOVER AUTOR DO PROJETO
    @DeleteMapping("/{id}/autores/{idAutor}")
    @Operation(summary = "Remover Autor do Projeto",
            description = "Remove um autor do projeto pelo ID do projeto e do autor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor removido do projeto com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto ou Autor não encontrado", content = @Content),
    })
    public ResponseEntity<?> removerAutor(@PathVariable Long projetoId, @PathVariable Long autorId) {
        try {
            Projeto atualizado = projetoService.removerAutor(projetoId, autorId);
            ProjetoResponseDTO response = ProjetoServiceIMP.toProjetoResponseDTO(atualizado);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //REMOVER AVALIAÇÃO DO PROJETO
    @DeleteMapping("/{id}/avaliacoes/{idAvaliacao}")
    @Operation(summary = "Remover Avaliação do Projeto",
            description = "Remove uma avaliação associada a um projeto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avaliação removida do projeto com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto ou Avaliação não encontrado", content = @Content),
    })
    public ResponseEntity<?> removerAvaliacaoDoProjeto(@PathVariable Long id, @PathVariable Long idAvaliacao) {
        try {
            Projeto atualizado = projetoService.removerAvaliacao(id, idAvaliacao);
            ProjetoResponseDTO response = ProjetoServiceIMP.toProjetoResponseDTO(atualizado);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Erro ao remover avaliação: " + e.getMessage());
        }
    }
}
