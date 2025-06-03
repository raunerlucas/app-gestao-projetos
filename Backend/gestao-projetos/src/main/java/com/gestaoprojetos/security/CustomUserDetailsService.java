package com.gestaoprojetos.security;


import com.gestaoprojetos.model.Usuario;
import com.gestaoprojetos.service.UsuarioServiceImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Adapter para o Spring Security buscar UserDetails a partir de nossa base de Usuario.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioServiceImpl usuarioService;

    public CustomUserDetailsService(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        // Aqui, sem roles; apenas USER comum
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // já está em hash BCrypt
                .roles("USER") // atribuímos apenas uma role genérica “USER”
                .build();
    }
}
