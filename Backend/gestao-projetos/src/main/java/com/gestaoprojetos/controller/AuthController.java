package com.gestaoprojetos.controller;

import com.gestaoprojetos.model.Pessoa;
import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.security.JwtUtil;
import com.gestaoprojetos.service.UsuarioServiceImpl;
import jakarta.validation.Valid;
import lombok.Data;
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

/**
 * Endpoints públicos para registro e login.
 */
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

    /**
     * DTO para receber dados de registro de usuário.
     */
    @Data
    public static class RegisterRequest {
        @Valid
        private String username;
        @Valid
        private String password;
        @Valid
        private Long pessoaId;
        // front Angular envia o ID da Pessoa (Autor ou Avaliador) que já exista
    }

    /**
     * Endpoint POST /auth/register
     * Recebe JSON { username, password, pessoaId }.
     * Tenta criar usuário; retorna 200 OK com usuário (exceto senha).
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
        // Cria instância de Usuario parcial para passar ao service
        Usuario novo = new Usuario();
        novo.setUsername(request.getUsername());
        novo.setPassword(request.getPassword());

        // Cria um placeholder de Pessoa apenas com ID; o service buscará a Pessoa real
        Pessoa pessoa = new Pessoa() {};
        pessoa.setId(request.getPessoaId());
        novo.setPessoa(pessoa);

        Usuario usuarioSalvo = usuarioService.registrarUsuario(novo);
        // Não retornamos o hash da senha porque @JsonIgnore em password cuida disso
        return ResponseEntity.ok(usuarioSalvo);
    }

    /**
     * DTO para login.
     */
    @Data
    public static class LoginRequest {
        @Valid
        private String username;
        @Valid
        private String password;
    }

    /**
     * Endpoint POST /auth/login
     * Recebe JSON { username, password }.
     * Autentica via AuthenticationManager e retorna { token: "xxx" }.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            // Tenta autenticar (dispara BadCredentialsException se falhar)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()
                    )
            );
            // Se chegou aqui, está autenticado: gera token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(401)
                    .body("Usuário ou senha inválidos");
        }
    }

    /**
     * DTO para resposta de login.
     */
    @Data
    public static class AuthResponse {
        private final String token;
    }
}
