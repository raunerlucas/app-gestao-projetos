package com.gestaoprojetos.security;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Define o PasswordEncoder como BCrypt (recomendado).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expõe AuthenticationManager para podermos autenticar manualmente no controller de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Define regras de segurança HTTP:
     * - /auth/login e /auth/register são públicas (permitAll())
     * - qualquer outro endpoint exige autenticação
     * - usa JWT (stateless)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // desativa CSRF (não usamos forms; estamos em API REST)
                .csrf().disable()
                // não cria sessão; cada requisição carrega token
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // configura quais endpoints são públicos
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                // opcional: permitir acesso a /h2-console/**, se usar banco H2 para testes
                // .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // filtra o token antes de processar as requisições
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Se estiver usando H2 e console, libere frame options:
        // http.headers().frameOptions().disable();

        return http.build();
    }
}
