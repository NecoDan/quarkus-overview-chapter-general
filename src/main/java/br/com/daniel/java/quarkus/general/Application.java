package br.com.daniel.java.quarkus.general;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.slf4j.Slf4j;

@QuarkusMain
@Slf4j
public class Application implements QuarkusApplication {

    public static void main(String... args) {
        log.info("Iniciando a aplicação Quarkus via main customizada...");
        // 2. Inicia o Quarkus e delega a execução para a classe que implementa QuarkusApplication
        Quarkus.run(Application.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        log.info("Aplicação Quarkus iniciada com sucesso!");

        // 3. Se for uma aplicação web/REST, você pode aguardar os comandos de parada (bloqueia a thread principal)
        // Se for uma aplicação de linha de comando (CLI), você pode apenas retornar 0 para encerrar.
        Quarkus.waitForExit();

        return 0; // Código de saída (exit code)
    }
}
