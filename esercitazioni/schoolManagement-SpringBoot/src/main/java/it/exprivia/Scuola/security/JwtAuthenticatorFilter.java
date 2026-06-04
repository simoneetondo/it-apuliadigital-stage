package it.exprivia.Scuola.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticatorFilter extends OncePerRequestFilter {

    // SECURITY CHAIN

    // CONCETTO STATELESS:
    // il server non ricorda chi sei tra una chiamata e l'altra ma deve verificare ogni volta il token
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

// 1. Estraiamo l'Header "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // controlliamo che l'header non sia null o che non inizi con Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // estrarre token togliendo la parola "Bearer
        jwt = authHeader.substring(7); // rimuovere beaerer

        // estrarre username utilizzando il service di jwtService
        username = jwtService.extractUsername(jwt);

        // capiamo se abbiamo l'username e l'utente non è già autenticato nella richiesta
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // verifichiamo che il token NON sia scaduto
            if (!jwtService.isTokenExpired(jwt)) {


                String role = jwtService.excractRole(jwt);
                // si crea il biglietto d'ingresso
                // confermiamo l'utenticazione dell'utente e diciamo anche che ruolo ha
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, // l'utente
                        null, // credenziali, null perchè token già verificatro
                        List.of(authority) // authorities, per ora vuote
                );

                // registriamo i dettagli della richiesta ( ip, session, etc
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // aggiornare contesto di sicurezza
                // da questo momento per spring l'utente è autenticato
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        filterChain.doFilter(request, response);
    }
}

