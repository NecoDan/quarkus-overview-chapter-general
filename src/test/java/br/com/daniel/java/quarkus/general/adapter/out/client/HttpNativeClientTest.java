package br.com.daniel.java.quarkus.general.adapter.out.client;

import br.com.daniel.java.quarkus.general.exceptions.api.HttpRestClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
class HttpNativeClientTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpResponse<Void> httpResponse;

    @Mock
    private HttpNativeManagerClient httpNativeManagerClient;

    @InjectMocks
    private HttpNativeClient httpNativeClient;

    private final String testUrl = "https://api.exemplo.com/recurso/1";
    private String bearerToken;

    @BeforeEach
    void setUp() {
        bearerToken = UUID.randomUUID() + UUID.randomUUID().toString() + UUID.randomUUID() + UUID.randomUUID();
    }

//    @Test
    @DisplayName("Deve executar a requisição DELETE com sucesso quando a API retornar sem erros")
    void deveExecutarDeleteComSucesso() throws Exception {
        // Arrange
        Map<String, String> headers = Map.of("Authorization", "Bearer Token" + bearerToken);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        // Act & Assert (Garante que nenhuma exceção é lançada)
        assertDoesNotThrow(() ->
                httpNativeClient.delete(testUrl, headers)
        );

        // Verify (Confirma que o método send do HttpClient foi chamado exatamente 1 vez)
        verify(httpClient, times(1))
                .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

//    @Test
    @DisplayName("Deve lançar HttpRestClientException quando ocorrer erro de IO ou interrupção no DELETE")
    void deveLancarExceptionQuandoFalharODelete() throws Exception {
        // Arrange
        Map<String, String> headers = Map.of("Authorization", "Bearer token-123");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Falha de conexão com o servidor"));

        // Act & Assert
        HttpRestClientException exception = assertThrows(
                HttpRestClientException.class,
                () -> httpNativeClient.delete(testUrl, headers)
        );

        // Valida se a mensagem da exceção personalizada contém os detalhes esperados
        assertTrue(exception.getMessage().contains(testUrl));
        assertTrue(exception.getMessage().contains("Falha de conexão com o servidor"));

        verify(httpClient, times(1))
                .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }
}