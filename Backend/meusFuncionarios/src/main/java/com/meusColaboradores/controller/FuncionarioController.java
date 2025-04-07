package com.meusColaboradores.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funcionarios")
@Tag(name = "Funcionario", description = "CRUD de Funcionarios")
public class FuncionarioController {

// TODO: Implementar CRUD de Funcionarios
    @GetMapping("/saudacao")
    @Operation(
            summary = "Retorna uma saudação",
            description = "Recebe um nome e retorna 'Olá, {nome}!'"
    )
    @ApiResponse(responseCode = "200", description = "Saudação retornada com sucesso")
    @ApiResponse(responseCode = "400", description = "Algo de errado não está certo")
    public String saudacao(@RequestParam(defaultValue = "Mundo") String nome) {
        return "Olá, " + nome + "!";
    }
}