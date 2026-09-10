package br.com.daniel.java.quarkus.general.config.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MongoIndexConfig {

    private static final String NAME_COLLECTION_TB_ORDER = "tb_btg_orders";

    @ConfigProperty(name = "quarkus.mongodb.database")
    String databaseName;

    @Inject
    MongoClient mongoClient;

    void onStart(@Observes StartupEvent ev) {
        defineConfigIndexsOrderCollection();
    }

    private void defineConfigIndexsOrderCollection() {
        MongoCollection<Document> orderCollections = mongoClient
                .getDatabase(databaseName)
                .getCollection(NAME_COLLECTION_TB_ORDER);

        // 1. Índice Simples Ascendente no campo "data_criacao"
        orderCollections.createIndex(Indexes.ascending("createdAt"));

        // 2. Índice Único no campo "ordeId (idPedidoExterno)"
        orderCollections.createIndex(
                Indexes.ascending("orderId"),
                new IndexOptions().unique(true)
        );

        // 3. Índice Composto (data_criacao ASC, valor_total DESC)
        orderCollections.createIndex(
                Indexes.compoundIndex(
                        Indexes.ascending("createdAt"),
                        Indexes.descending("totalValue")
                )
        );
    }
}

