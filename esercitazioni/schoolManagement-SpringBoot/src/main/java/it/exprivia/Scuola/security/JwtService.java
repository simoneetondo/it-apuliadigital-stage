package it.exprivia.Scuola.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.exprivia.Scuola.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    // LOGICA TOKEN

    // JWT composta da tre parti separate
    // 1. HEADER: contiene informazioni su come è stato creato il token e l'algoritmo di firma
    // 2. PAYLOAD: contiene le informazioni dell'utente( username, ruolo, scadenza)
    // 3. SIGNATURE: è la parte che garantisce integrità del token, quello che VerifyWith controlla per essere sicuro
    //    che il payload non è alterato.

    private final JwtConfig jwtConfig;

    // 1. Creiamo il token
    public String generateToken(String username, String role) {
        // costruiamo il token
        return Jwts.builder()
                .claim("role", role) // Inseriamo il ruolo
                // definisce a chi appartiene il token, username/email
                .subject(username)   // Inseriamo l'utente
                // momento esatto in cui si crea il token
                .issuedAt(new Date())
                // imposta data scadenza del token prendendo l'ora attuale + il valore di jwtconfig
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                // una chiave segreta per firmare il token che solo il nostr server sa, se cambiata il token non è piu valido
                .signWith(getSignInKey())
                // impacchetta tutto in una stringa composta da tre parti separate da punti header/payload/signature
                .compact();
    }

    // 2. Leggiamo lo username dal token
    public String extractUsername(String token) {
        // inizializza lo strumento di analisi
        Claims claims = Jwts.parser()
                // dice al parser di prendere esattamente quella key
                .verifyWith(getSignInKey())
                // configura il parser con le impostazioni che noi li abbiamo dato
                .build()
                // il parser prende la stringa e verifica che la firma è valida,
                // se è scaduta ad esempio lancia un eccezione,
                // e la scompone in un json originale
                .parseSignedClaims(token)
                // quà prende la parte centrale, esattamente dove contiene l'username, ruolo e scadenza
                .getPayload();

        return claims.getSubject();
    }

    // 3. Verifichiamo se è scaduto
    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration().before(new Date());
    }

    // questo prende la stringa che abbiamo definito nell'application-properties. (lunga,complessa,sicura)
    private SecretKey getSignInKey() {
        // Usa direttamente i bytes della stringa
        // la crittografia non lavora con le lettere ma con i byte utilizzando la codifica standard UTF_8
        byte[] keyBytes = jwtConfig.getSecretKey().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        // questa è la funzione della libreria, prende i byte e crea un oggetto SecretKey
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String excractRole(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }

}