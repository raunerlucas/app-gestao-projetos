package com.gestaoprojetos.controller;

import com.gestaoprojetos.controller.DTO.AutorDTO.AutorResponseDTO;
import com.gestaoprojetos.exception.BadRequestException;
import com.gestaoprojetos.exception.ResourceNotFoundException;
import com.gestaoprojetos.model.Autor;
import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.service.AutorServiceIMP;
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

import static com.gestaoprojetos.controller.DTO.AutorDTO.AutorRequestDTO;

@RestController
@RequestMapping("/autores")
public class AutorController {
    private final AutorServiceIMP autorService;

    @Autowired
    public AutorController(AutorServiceIMP autorServiceIMP) {
        this.autorService = autorServiceIMP;
    }

    /**
     * Endpoint para obter todos os autores.
     *
     * @return ResponseEntity com a lista de autores.
     */
    // Listar todos os autores
    @GetMapping
    @Operation(summary = "Listar Autores",
            description = "Retorna todos os autores cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de autores obtida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AutorResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum autor encontrado", content = @Content),
    })
    public ResponseEntity<List<AutorResponseDTO>> LazyListarTodos() {
        List<AutorResponseDTO> autores = autorService.listarTodos();
        if (autores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

    /**
     * Endpoint para obter um autor por ID.
     *
     * @param id ID do autor a ser buscado.
     * @return ResponseEntity com o autor encontrado ou 404 se não existir.
     */
    // Buscar autor por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar Autor por ID",
            description = "Busca um autor pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class))),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado", content = @Content),
    })
    public ResponseEntity<Autor> buscarPorId(@PathVariable Long id) {
        Autor autor = autorService.buscarPorId(id);
        return ResponseEntity.ok(autor);
    }

    /**
     * Endpoint para obter um autor por ID.
     *
     * @param id ID do autor a ser buscado.
     * @return ResponseEntity com o autor encontrado ou 404 se não existir.
     */
    // Buscar autor por ID
    @GetMapping("/lazy/{id}")
    @Operation(summary = "Buscar Autor por ID",
            description = "Busca um autor pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AutorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado", content = @Content),
    })
    public ResponseEntity<AutorResponseDTO> LazyBuscarPorId(@PathVariable Long id) {
        try {
            AutorResponseDTO autor = autorService.LazyBuscarPorId(id);
            return ResponseEntity.ok(autor);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para criar um novo autor.
     *
     * @param autor Dados do autor a ser criado.
     * @return ResponseEntity com o autor criado.
     */
    // Criar autor
    @PostMapping
    @Operation(summary = "Criar Autor",
            description = "Cria um novo autor com os dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AutorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do autor", content = @Content)})
    public ResponseEntity<?> criarAutor(@RequestBody @Valid AutorRequestDTO autor) {
        try {
            Autor novoAutor = autorService.criarAutor(autor);
            return ResponseEntity.created(URI.create("/autores/" + novoAutor.getId())).body(novoAutor);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Erro ao criar autor: " + e.getMessage());
        }
    }

    /**
     * Endpoint para associar um projeto a um autor.
     *
     * @param autorId ID do autor ao qual o projeto será associado.
     * @param projeto Projeto a ser associado ao autor.
     * @return ResponseEntity com o autor atualizado ou 404 se não existir.
     */
    // Associar projeto ao autor
    @PostMapping("{autorId}/projetos")
    @Operation(summary = "Associar Projeto ao Autor",
            description = "Associa um projeto existente a um autor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto associado ao autor com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ID do Projeto ausente/inválido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor ou Projeto não encontrado", content = @Content),
    })
    public ResponseEntity<Autor> adicionarProjetoAoAutor(@PathVariable Long autorId, @RequestBody Projeto projeto) {
        Autor autorAtualizado = autorService.adicionarProjeto(autorId, projeto);
        return ResponseEntity.ok(autorAtualizado);
    }

    /**
     * Endpoint para atualizar um autor existente.
     *
     * @param id    ID do autor a ser atualizado.
     * @param autor Dados atualizados do autor.
     * @return ResponseEntity com o autor atualizado ou 404 se não existir.
     */
    // Atualizar autor
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Autor",
            description = "Atualiza os dados de um autor existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class))),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização do autor", content = @Content),
    })
    public ResponseEntity<Autor> atualizarAutor(@PathVariable Long id, @RequestBody Autor autor) {
        Autor novoAutor = autorService.atualizarAutor(id, autor);
        return ResponseEntity.ok(novoAutor);
    }

    /**
     * Endpoint para excluir um autor por ID.
     *
     * @param id ID do autor a ser excluído.
     * @return ResponseEntity com status 204 (No Content) se excluído com sucesso ou 404 se não existir.
     */
    // Excluir autor
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Autor", description = "Exclui um autor do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado", content = @Content)
    })
    public ResponseEntity<?> excluirAutor(@PathVariable Long id) {
        autorService.deletarPorId(id);
        return ResponseEntity.status(204).build();
    }

    /**
     * Endpoint para remover um projeto de um autor.
     *
     * @param autorId   ID do autor do qual o projeto será removido.
     * @param projetoId ID do projeto a ser removido do autor.
     * @return ResponseEntity com o autor atualizado ou 404 se o autor ou projeto não existir.
     */
    //remover projeto do autor
    @DeleteMapping("/{autorId}/projetos/{projetoId}")
    @Operation(summary = "Remover Projeto de Autor",
            description = "Desassociar um Projeto de um Autor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto removido do Autor com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class))),
            @ApiResponse(responseCode = "404", description = "Autor, Projeto, ou associação não encontrada", content = @Content),
    })
    public ResponseEntity<Autor> removerProjetoDoAutor(@PathVariable Long autorId, @PathVariable Long projetoId) {
        Autor autorAtualizado = autorService.removerProjeto(autorId, projetoId);
        return ResponseEntity.ok(autorAtualizado);
    }
}
