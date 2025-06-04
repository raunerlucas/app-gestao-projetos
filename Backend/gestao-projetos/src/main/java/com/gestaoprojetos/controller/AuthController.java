package com.gestaoprojetos.controller;

import com.gestaoprojetos.model.Pessoa;
import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.security.JwtUtil;
import com.gestaoprojetos.service.UsuarioServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioServiceImpl usuarioService;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthenticationManager authenticationManager,
            UsuarioServiceImpl usuarioService,
            JwtUtil jwtUtil
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private Long pessoaId;
    }

    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário",
            description = "Registra um novo usuário associado a uma pessoa existente. " +
                    "O ID da pessoa deve ser fornecido no corpo da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
        Usuario novo = new Usuario();
        novo.setUsername(request.getUsername());
        novo.setPassword(request.getPassword());

        // Associa Pessoa “placeholder” com só o ID; o service buscará a pessoa real
        Pessoa pessoa = new Pessoa() {};
        pessoa.setId(request.getPessoaId());
        novo.setPessoa(pessoa);

        Usuario usuarioSalvo = usuarioService.registrarUsuario(novo);
        return ResponseEntity.ok(usuarioSalvo);
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza o login do usuário",
            description = "Autentica o usuário e retorna um token JWT se as credenciais forem válidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido, token JWT retornado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }
    }

    @Data
    public static class AuthResponse {
        private final String message = "Login bem-sucedido";
        private final String tipoToken = "Bearer";
        private final String token;
    }
}
