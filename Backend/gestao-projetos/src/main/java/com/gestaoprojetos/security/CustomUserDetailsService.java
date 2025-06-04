package com.gestaoprojetos.security;


import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.service.UsuarioServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Adapter para o Spring Security buscar UserDetails a partir de nossa base de Usuario.
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioServiceImpl usuarioService;

    public CustomUserDetailsService(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }

    //    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Usuario usuario = usuarioService.buscarPorUsername(username)
//                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
//
//        // Aqui, sem roles; apenas USER comum
//        return User.builder()
//                .username(usuario.getUsername())
//                .password(usuario.getPassword()) // já está em hash BCrypt
//                .roles("USER") // atribuímos apenas uma role genérica “USER”
//                .build();
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(new Usuario(username, null, null));

        // Aqui, usuario.getPassword() deve retornar o hash (ex: $2a$10$...)
        return User
                .withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities("USER")
                .build();
    }
}
