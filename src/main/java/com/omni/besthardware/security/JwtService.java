package com.omni.besthardware.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    @Value("${app.jwt.secret:besthardware-av2-chave-local-desenvolvimento-precisa-ter-32-bytes}")
    private String secret;

    @Value("${app.jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    public String gerarToken(UserDetails userDetails) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(expirationSeconds);

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", userDetails.getUsername());
        payload.put("roles", permissoes(userDetails));
        payload.put("iat", issuedAt.getEpochSecond());
        payload.put("exp", expiresAt.getEpochSecond());

        String unsignedToken = base64Url(jsonBytes(header)) + "." + base64Url(jsonBytes(payload));
        return unsignedToken + "." + assinar(unsignedToken);
    }

    public String extrairUsuario(String token) {
        return (String) lerPayloadValidado(token).get("sub");
    }

    public boolean tokenValido(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extrairUsuario(token));
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    private List<String> permissoes(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    private Map<String, Object> lerPayloadValidado(String token) {
        String[] partes = token.split("\\.");
        if (partes.length != 3) {
            throw new BadCredentialsException("Token JWT invalido.");
        }

        String unsignedToken = partes[0] + "." + partes[1];
        String assinaturaEsperada = assinar(unsignedToken);
        if (!MessageDigest.isEqual(
                assinaturaEsperada.getBytes(StandardCharsets.US_ASCII),
                partes[2].getBytes(StandardCharsets.US_ASCII)
        )) {
            throw new BadCredentialsException("Assinatura JWT invalida.");
        }

        Map<String, Object> payload = lerJson(Base64.getUrlDecoder().decode(partes[1]));
        Object exp = payload.get("exp");
        if (!(exp instanceof Number expiration) || Instant.now().getEpochSecond() > expiration.longValue()) {
            throw new BadCredentialsException("Token JWT expirado.");
        }

        return payload;
    }

    private String assinar(String conteudo) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return base64Url(mac.doFinal(conteudo.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Nao foi possivel assinar o token JWT.", exception);
        }
    }

    private byte[] jsonBytes(Object value) {
        try {
            return MAPPER.writeValueAsBytes(value);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Nao foi possivel gerar JSON do token JWT.", exception);
        }
    }

    private Map<String, Object> lerJson(byte[] bytes) {
        try {
            return MAPPER.readValue(bytes, MAP_TYPE);
        } catch (Exception exception) {
            throw new BadCredentialsException("Payload JWT invalido.", exception);
        }
    }

    private String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
