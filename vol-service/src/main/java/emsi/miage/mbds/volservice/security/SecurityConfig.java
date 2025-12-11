package emsi.miage.mbds.volservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Import nécessaire pour les méthodes HTTP
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injection du convertisseur que nous venons de créer
    private final JwtAuthConverter jwtAuthConverter;

    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // --- RÈGLES DE RÔLES ---
                        // Tout le monde (connecté) peut voir les vols
                        .requestMatchers(HttpMethod.GET, "/vols/**").hasAnyRole("USER", "ADMIN")

                        // Seul l'ADMIN peut créer ou modifier
                        .requestMatchers(HttpMethod.POST, "/vols/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/vols/**").hasRole("ADMIN")

                        // Toute autre requête nécessite juste d'être authentifié
                        .anyRequest().authenticated()
                )

                // Configuration OAuth2 avec notre convertisseur personnalisé
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)) // <--- C'est ici que la magie opère
                );

        return http.build();
    }
}