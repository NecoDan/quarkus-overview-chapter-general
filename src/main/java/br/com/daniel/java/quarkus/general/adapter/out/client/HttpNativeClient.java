package br.com.daniel.java.quarkus.general.adapter.out.client;

import br.com.daniel.java.quarkus.general.exceptions.api.HttpRestClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.HttpMethod;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Slf4j
public class HttpNativeClient {

    @Inject
    ObjectMapper objectMapper;

    private final HttpClient httpClient;

    public HttpNativeClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * Realiza uma requisição HTTP do tipo GET para a URL especificada e desserializa
     * a resposta JSON para o tipo de classe fornecido.
     *
     * @param <T>          O tipo da classe de destino para o qual a resposta será desserializada.
     * @param url          A URL de destino da requisição GET.
     * @param responseType A classe do objeto de retorno (`Class<T>`), usada pelo Jackson.
     * @param headers      Um mapa contendo os cabeçalhos HTTP adicionais (opcional).
     * @return O objeto desserializado do tipo {@code T} representando a resposta da API.
     * @throws HttpRestClientException Se ocorrer qualquer falha durante a execução da chamada HTTP,
     *                                 conexão ou no processo de desserialização do JSON.
     */
    public <T> T get(String url,
                     Class<T> responseType,
                     Map<String, String> headers) {
        try {
            HttpRequest request = buildRequest(url,
                    HttpMethod.GET,
                    HttpRequest.BodyPublishers.noBody(),
                    headers
            );

            return sendAndDeserialize(request, responseType);
        } catch (Exception e) {
            log.error("Erro ao executar requisição GET para a URL: [{}]. Causa: {}", url, e.getMessage());

            throw new HttpRestClientException("Erro ao executar requisição GET para a URL: [%s]. Causa: %s"
                    .formatted(url, e.getMessage())
                    , e
            );
        }
    }

    /**
     * Executa uma requisição HTTP POST para a URL especificada, enviando um corpo no formato JSON
     * e desserializando a resposta para o tipo de classe informado.
     *
     * @param <T>          O tipo da classe de resposta esperada.
     * @param <R>          O tipo do objeto enviado no corpo (payload) da requisição.
     * @param url          A URL de destino da requisição.
     * @param body         O objeto que será serializado para JSON e enviado no corpo da requisição.
     * @param responseType A classe do objeto de resposta para a qual o JSON será convertido.
     * @param headers      Um mapa contendo os cabeçalhos HTTP adicionais (opcional).
     * @return Uma instância do tipo de resposta especificado contendo os dados retornados pela API.
     * @throws HttpRestClientException Se ocorrer qualquer erro durante o envio, processamento ou desserialização da resposta.
     */
    public <T, R> T post(String url,
                         R body,
                         Class<T> responseType,
                         Map<String, String> headers) {

        try {
            HttpRequest request = buildRequest(url,
                    HttpMethod.POST,
                    toJsonBody(body),
                    headers
            );

            return sendAndDeserialize(request, responseType);
        } catch (Exception e) {
            log.error("Falha crítica ao tentar processar a chamada POST para o endpoint: [{}]. Detalhes do erro: {}", url, e.getMessage(), e);

            throw new HttpRestClientException("Erro ao executar requisição POST para a URL: [%s]. Causa: %s"
                    .formatted(url, e.getMessage())
                    , e
            );
        }
    }

    /**
     * Realiza uma requisição HTTP PUT para a URL especificada, enviando um corpo serializado em JSON
     * e desserializando a resposta para o tipo informado.
     *
     * @param <T>          O tipo da classe de resposta esperada.
     * @param <R>          O tipo da classe do objeto enviado no corpo da requisição.
     * @param url          A URL de destino da requisição.
     * @param body         O objeto que será serializado e enviado no corpo (payload).
     * @param responseType A classe correspondente ao tipo de dado esperado na resposta.
     * @param headers      Um mapa contendo os cabeçalhos HTTP adicionais (opcional).
     * @return O objeto desserializado correspondente à resposta da requisição.
     * @throws HttpRestClientException Se ocorrer qualquer erro durante o envio da requisição,
     *                                 serialização, desserialização ou se o servidor retornar um erro.
     */
    public <T, R> T put(String url,
                        R body,
                        Class<T> responseType,
                        Map<String, String> headers) {

        try {
            HttpRequest request = buildRequest(url,
                    HttpMethod.PUT,
                    toJsonBody(body),
                    headers
            );

            return sendAndDeserialize(request, responseType);
        } catch (Exception e) {
            log.error("Falha crítica ao tentar processar a requisição PUT. Destino: [{}], Payload enviado: [{}], Erro: [{}]",
                    url, body, e.getMessage(), e);

            throw new HttpRestClientException("Erro ao executar requisição PUT para a URL: [%s]. Causa: %s"
                    .formatted(url, e.getMessage())
                    , e
            );
        }
    }

    /**
     * Realiza uma requisição HTTP do tipo DELETE para a URL especificada.
     *
     * <p>Este método descarta o corpo da resposta HTTP recebida do servidor, focando
     * apenas no status de sucesso da operação (códigos HTTP 2xx).</p>
     *
     * @param url     A URL de destino para a qual a requisição DELETE será enviada.
     * @param headers Um mapa contendo os cabeçalhos HTTP adicionais (opcional).
     * @throws HttpRestClientException Se ocorrer qualquer falha durante o envio da requisição,
     *                                 problemas de rede, timeout ou erro de I/O.
     */
    public void delete(String url,
                       Map<String, String> headers) {
        try {
            HttpRequest request = buildRequest(url,
                    HttpMethod.DELETE,
                    HttpRequest.BodyPublishers.noBody(),
                    headers
            );

            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            log.error("Falha crítica ao tentar processar a exclusão (DELETE). " +
                    "Verifique a conectividade com o destino. URL: [{}]. Detalhes do erro: {}", url, e.getMessage());

            throw new HttpRestClientException("Erro ao executar requisição DELETE para a URL: [%s]. Causa: %s"
                    .formatted(url, e.getMessage()),
                    e
            );
        }
    }

    /**
     * Realiza uma requisição PATCH.
     */
    public <T, R> T patch(String url,
                          R body,
                          Class<T> responseType,
                          Map<String, String> headers) {
        try {
            HttpRequest request = buildRequest(url,
                    HttpMethod.PATCH,
                    toJsonBody(body),
                    headers
            );

            return sendAndDeserialize(request, responseType);
        } catch (Exception e) {
            log.error("Erro ao executar requisição PATCH para a URL: [{}]. Causa: {}", url, e.getMessage());
            throw new HttpRestClientException("Erro ao executar requisição PATCH para a URL: [%s]. Causa: %s"
                    .formatted(url, e.getMessage())
                    , e
            );
        }
    }

    /**
     * Constrói e configura uma instância de {@link HttpRequest} pronta para ser disparada
     * pelo {@link HttpClient}.
     *
     * <p>Este método configura automaticamente a URI destino, o método HTTP (GET, POST, etc.),
     * o corpo da requisição e insere cabeçalhos padrão de comunicação (como {@code Content-Type}
     * e {@code Accept} configurados para {@code application/json}), permitindo também a injeção
     * de cabeçalhos customizados adicionais.</p>
     *
     * @param url           A string contendo a URL de destino da requisição. Será convertida para {@link URI}.
     * @param method        O verbo HTTP a ser utilizado (ex: {@code "GET"}, {@code "POST"}, {@code "PUT"}, etc.).
     * @param bodyPublisher O publicador do corpo da requisição gerado por {@link HttpRequest.BodyPublishers},
     *                      necessário para métodos que enviam payload (pode ser vazio para GET/DELETE).
     * @param headers       Um mapa opcional contendo pares de chave-valor para cabeçalhos HTTP customizados
     *                      que devem ser adicionados ou sobrescritos na requisição.
     * @return Um objeto {@link HttpRequest} totalmente configurado e pronto para envio.
     * @throws IllegalArgumentException se a string da URL fornecida for nula ou inválida.
     */
    private HttpRequest buildRequest(String url,
                                     String method,
                                     HttpRequest.BodyPublisher bodyPublisher,
                                     Map<String, String> headers) {

        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url)).method(method, bodyPublisher);

        // Adiciona cabeçalhos padrão se não existirem
        builder.header("Content-Type", "application/json");
        builder.header("Accept", "application/json");

        // Adiciona cabeçalhos customizados, se houver
        if (headers != null) {
            headers.forEach(builder::header);
        }

        return builder.build();
    }

    /**
     * Serializa um objeto Java em formato JSON para ser utilizado como corpo (payload) de uma requisição HTTP.
     *
     * <p>Caso o objeto fornecido seja {@code null}, este método retornará um
     * {@link HttpRequest.BodyPublisher} indicando que a requisição não possui corpo.</p>
     *
     * @param body o objeto Java a ser serializado em JSON; pode ser {@code null}.
     * @return um {@link HttpRequest.BodyPublisher} contendo a representação em JSON do objeto
     * ou um publicador sem corpo caso o parâmetro seja nulo.
     * @throws HttpRestClientException se ocorrer algum erro durante o processo de serialização do objeto para JSON.
     */
    private HttpRequest.BodyPublisher toJsonBody(Object body) {
        try {
            if (body == null) {
                return HttpRequest.BodyPublishers.noBody();
            }
            String json = objectMapper.writeValueAsString(body);
            return HttpRequest.BodyPublishers.ofString(json);
        } catch (Exception e) {
            throw new HttpRestClientException("Erro ao serializar corpo da requisição para JSON", e);
        }
    }

    /**
     * Envia uma requisição HTTP de forma síncrona, valida se o código de status está
     * na faixa de sucesso (2xx) e desserializa o corpo da resposta para o tipo especificado.
     *
     * <p>Se o tipo de retorno informado for {@link String}, o corpo da resposta é retornado
     * diretamente como texto sem realizar operações de desserialização JSON.</p>
     *
     * @param <T>          O tipo genérico do objeto de destino esperado após a desserialização.
     * @param request      O objeto {@link HttpRequest} contendo os dados da requisição configurada
     *                     (método, URI, headers e corpo).
     * @param responseType A classe {@link Class} representando o tipo de dado para o qual o corpo
     *                     da resposta será convertido.
     * @return Uma instância do tipo {@link T} contendo a resposta desserializada ou
     * a string bruta caso o tipo seja {@link String}.
     * @throws HttpRestClientException Se ocorrer qualquer erro durante o envio da requisição de rede,
     *                                 se a resposta retornar um código HTTP fora do intervalo de sucesso
     *                                 (diferente de 2xx), ou se houver falhas no processo de desserialização JSON.
     */
    private <T> T sendAndDeserialize(HttpRequest request,
                                     Class<T> responseType) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Validação simples de status HTTP de sucesso (2xx)
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                final var errorMessage = "Falha na requisição HTTP. Status: %s | Resposta: %s"
                        .formatted(response.statusCode(), response.body());
                throw new HttpRestClientException(errorMessage);
            }

            // Se o tipo de retorno for String, retorna diretamente sem desserializar
            if (responseType.equals(String.class)) {
                return responseType.cast(response.body());
            }

            return objectMapper.readValue(response.body(), responseType);

        } catch (Exception e) {
            throw new HttpRestClientException("Erro ao executar chamada HTTP: %s".formatted(e.getMessage()), e);
        }
    }

}
