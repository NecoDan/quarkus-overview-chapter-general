package br.com.daniel.java.quarkus.general.config;

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
    private static final String NAME_COLLECTION_TB_ORDER_ITEM = "tb_btg_order_items";

    @ConfigProperty(name = "quarkus.mongodb.database")
    String databaseName;

    @Inject
    MongoClient mongoClient;

    void onStart(@Observes StartupEvent ev) {
        defineConfigIndexsOrderCollection();
        defineConfigIndexsOrderItemCollection();

        // 4. Índice TTL (expira documentos após X segundos no campo "dataExpiracao")
        //        itemsOrderCollections.createIndex(
        //                Indexes.ascending("dataExpiracao"),
        //                new IndexOptions().expireAfter(30L, TimeUnit.DAYS)
        //        );
    }

    private void defineConfigIndexsOrderCollection() {
        MongoCollection<Document> orderCollections = mongoClient
                .getDatabase(databaseName)
                .getCollection(NAME_COLLECTION_TB_ORDER);

        // 1. Índice Simples Ascendente no campo "data_criacao"
        orderCollections.createIndex(Indexes.ascending("data_criacao"));

        // 2. Índice Único no campo "id_cliente"
        orderCollections.createIndex(
                Indexes.ascending("id_cliente"),
                new IndexOptions().unique(true)
        );

        // 3. Índice Composto (data_criacao ASC, valor_total DESC)
        orderCollections.createIndex(
                Indexes.compoundIndex(
                        Indexes.ascending("data_criacao"),
                        Indexes.descending("valor_total")
                )
        );
    }

    private void defineConfigIndexsOrderItemCollection() {
        MongoCollection<Document> orderItemCollections = mongoClient
                .getDatabase(databaseName)
                .getCollection(NAME_COLLECTION_TB_ORDER_ITEM);

        // 5. Índice de Texto para busca textual
        orderItemCollections.createIndex(Indexes.text("produto"));
    }
}

