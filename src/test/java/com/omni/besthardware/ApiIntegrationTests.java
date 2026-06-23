package com.omni.besthardware;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiIntegrationTests {

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String adminToken;
    private String usuarioToken;

    @Test
    void deveExporDocumentacaoOpenApi() throws Exception {
        HttpResponse<String> response = getPublico("/v3/api-docs");

        assertEquals(200, response.statusCode());

        JsonNode body = objectMapper.readTree(response.body());
        assertEquals("Best Hardware API", body.path("info").path("title").asText());
        assertNotNull(body.path("paths").path("/api/cpus"));
    }

    @Test
    void deveBloquearApiSemToken() throws Exception {
        HttpResponse<String> response = getPublico("/api/cpus");

        assertEquals(401, response.statusCode());
    }

    @Test
    void deveAutenticarUsuarioEEmitirJwt() throws Exception {
        HttpResponse<String> response = login("usuario", "usuario123");

        assertEquals(200, response.statusCode());

        JsonNode body = objectMapper.readTree(response.body());
        assertFalse(body.path("token").asText().isBlank());
        assertEquals("Bearer", body.path("tipo").asText());
        assertEquals("usuario", body.path("usuario").asText());
    }

    @Test
    void deveFiltrarCpusPorSocketENucleos() throws Exception {
        HttpResponse<String> response = get("/api/cpus?socket=AM4&nucleosMinimos=6");

        assertEquals(200, response.statusCode());

        JsonNode cpus = objectMapper.readTree(response.body());
        assertTrue(cpus.isArray());
        assertFalse(cpus.isEmpty());

        for (JsonNode cpu : cpus) {
            assertEquals("AM4", cpu.path("socket").asText());
            assertTrue(cpu.path("nucleos").asInt() >= 6);
        }
    }

    @Test
    void deveCriarOrcamentoComItens() throws Exception {
        String requestBody = """
                {
                  "nome": "PC teste integracao",
                  "usuarioId": 1,
                  "perfilId": 1,
                  "itens": [
                    {
                      "componenteId": 1,
                      "quantidade": 1
                    },
                    {
                      "componenteId": 3,
                      "quantidade": 1
                    }
                  ]
                }
                """;

        HttpResponse<String> response = post("/api/orcamentos", requestBody);

        assertEquals(201, response.statusCode());

        JsonNode orcamento = objectMapper.readTree(response.body());
        assertTrue(orcamento.path("id").asInt() > 0);
        assertEquals("PC teste integracao", orcamento.path("nome").asText());
        assertEquals(1, orcamento.path("usuarioId").asInt());
        assertEquals(1, orcamento.path("perfilId").asInt());
        assertEquals(2, orcamento.path("itens").size());
        assertTrue(orcamento.path("precoTotal").decimalValue().signum() > 0);

        JsonNode itemCpu = buscarItemPorComponente(orcamento.path("itens"), 1);
        assertNotNull(itemCpu);
        assertEquals(0, itemCpu.path("precoUnitario").decimalValue().compareTo(new BigDecimal("829.90")));
    }

    @Test
    void deveListarOfertasDePrecoPorComponente() throws Exception {
        HttpResponse<String> response = get("/api/ofertas-preco?componenteId=1");

        assertEquals(200, response.statusCode());

        JsonNode ofertas = objectMapper.readTree(response.body());
        assertTrue(ofertas.isArray());
        assertFalse(ofertas.isEmpty());

        for (JsonNode oferta : ofertas) {
            assertEquals(1, oferta.path("componenteId").asInt());
            assertTrue(oferta.path("precoAvista").decimalValue().signum() > 0);
        }
    }

    @Test
    void deveRetornarMenorOfertaPorComponente() throws Exception {
        HttpResponse<String> response = get("/api/ofertas-preco/componentes/1/menor-preco");

        assertEquals(200, response.statusCode());

        JsonNode oferta = objectMapper.readTree(response.body());
        assertEquals(1, oferta.path("componenteId").asInt());
        assertEquals(0, oferta.path("precoAvista").decimalValue().compareTo(new BigDecimal("829.90")));
    }

    @Test
    void deveRetornarErroPadronizadoQuandoCpuNaoExiste() throws Exception {
        HttpResponse<String> response = get("/api/cpus/999999");

        assertEquals(404, response.statusCode());

        JsonNode erro = objectMapper.readTree(response.body());
        assertEquals(404, erro.path("status").asInt());
        assertTrue(erro.path("mensagem").asText().contains("CPU"));
        assertEquals("/api/cpus/999999", erro.path("caminho").asText());
    }

    @Test
    void deveValidarParametroDeRota() throws Exception {
        HttpResponse<String> response = get("/api/cpus/0");

        assertEquals(400, response.statusCode());

        JsonNode erro = objectMapper.readTree(response.body());
        assertEquals(400, erro.path("status").asInt());
        assertTrue(erro.path("mensagem").asText().contains("parametros invalidos"));
    }

    private HttpResponse<String> get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .header("Authorization", "Bearer " + usuarioToken())
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> post(String path, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken())
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> getPublico(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> login(String username, String password) throws IOException, InterruptedException {
        String requestBody = """
                {
                  "username": "%s",
                  "password": "%s"
                }
                """.formatted(username, password);

        HttpRequest request = HttpRequest.newBuilder(uri("/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private String adminToken() throws IOException, InterruptedException {
        if (adminToken == null) {
            adminToken = extrairToken(login("admin", "admin123"));
        }
        return adminToken;
    }

    private String usuarioToken() throws IOException, InterruptedException {
        if (usuarioToken == null) {
            usuarioToken = extrairToken(login("usuario", "usuario123"));
        }
        return usuarioToken;
    }

    private String extrairToken(HttpResponse<String> response) throws IOException {
        assertEquals(200, response.statusCode());
        return objectMapper.readTree(response.body()).path("token").asText();
    }

    private URI uri(String path) {
        return URI.create("http://localhost:" + port + path);
    }

    private JsonNode buscarItemPorComponente(JsonNode itens, int componenteId) {
        for (JsonNode item : itens) {
            if (item.path("componenteId").asInt() == componenteId) {
                return item;
            }
        }

        return null;
    }
}
