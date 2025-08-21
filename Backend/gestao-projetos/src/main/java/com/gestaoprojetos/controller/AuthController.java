package com.gestaoprojetos.controller;

import com.gestaoprojetos.controller.DTO.UsuarioDTO;
import com.gestaoprojetos.controller.DTO.UsuarioDTO.UsuarioResponseDTO;
import com.gestaoprojetos.model.Pessoa;
import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.security.JwtUtil;
import com.gestaoprojetos.service.UsuarioServiceIMP;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioServiceIMP usuarioService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            UsuarioServiceIMP usuarioService,
            JwtUtil jwtUtil
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }


    /**
     * Endpoint para registrar um novo usuário associado a uma pessoa existente.
     * O ID da pessoa deve ser fornecido no corpo da requisição.
     *
     * @param request Dados do usuário a ser registrado
     * @return Resposta com o status do registro
     */
    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário",
            description = "Registra um novo usuário associado a uma pessoa existente. " +
                    "O ID da pessoa deve ser fornecido no corpo da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content()),
    })
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        Long pessoaId = request.getPessoaId();

        if (username == null || username.isEmpty() || password == null || password.isEmpty() || pessoaId == null) {
            return ResponseEntity.badRequest().body("Dados inválidos para registro de usuário");
        }


        Usuario novo = new Usuario();
        novo.setUsername(username);
        novo.setPassword(password);

        // Associa Pessoa “placeholder” com só o ID; o service buscará a pessoa real
        Pessoa pessoa = new Pessoa() {};
        pessoa.setId(pessoaId);
        novo.setPessoa(pessoa);

        try {
            Usuario usuarioSalvo = usuarioService.registrarUsuario(novo);
            var response = new UsuarioDTO.UsuarioResumoDTO(usuarioSalvo.getId(), usuarioSalvo.getUsername());
            return ResponseEntity.created(URI.create("/usuarios/" + usuarioSalvo.getId())).body(response);
        } catch (Exception ex) {
            log.error("Erro ao registrar usuário", ex);
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + ex.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza o login do usuário",
            description = "Autentica o usuário e retorna um token JWT se as credenciais forem válidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido, token JWT retornado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content())
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

    /**
     * Endpoint para validar o token JWT.
     * Retorna uma mensagem de sucesso se o token for válido.
     *
     * @param token Token JWT a ser validado
     * @return Resposta com o status da validação
     */
    @PostMapping("/validate")
    @Operation(summary = "Valida o token JWT",
            description = "Valida o token JWT fornecido e retorna uma mensagem de sucesso se o token for válido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token válido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token Invalido", content = @Content())
    })
    public ResponseEntity<?> validateToken(@RequestBody String token, @RequestBody String Username) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido ou ausente");
        }
        if (!jwtUtil.validateToken(token, Username)) {
            return ResponseEntity.status(401).body("Token inválido");
        }
        return ResponseEntity.ok("Token válido");
    }

    @PostMapping("/refresh")
    @Operation(summary = "Atualiza um token JWT",
            description = "Recebe um token JWT e, se válido, gera um novo token com período de validade renovado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido", content = @Content())
    })
    public ResponseEntity<?> refreshToken(@RequestBody String token) {
        try {
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(401).body("Token inválido ou ausente");
            }

            String username = jwtUtil.getUsernameFromToken(token);
            if (username == null) {
                return ResponseEntity.status(401).body("Token inválido");
            }

            String newToken = jwtUtil.generateToken(username);
            return ResponseEntity.ok(new AuthResponse(newToken));
        } catch (Exception ex) {
            log.error("Erro ao atualizar token", ex);
            return ResponseEntity.status(401).body("Erro ao atualizar token: " + ex.getMessage());
        }
    }
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private Long pessoaId;
    }

    @Data
    public static class AuthResponse {
        private final String tipoToken = "Bearer";
        private final String token;
    }
}
