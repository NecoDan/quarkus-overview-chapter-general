package br.com.daniel.java.quarkus.general.util.other;

import io.quarkus.test.junit.QuarkusTestProfile;

public class CustomH2DbTestProfile implements QuarkusTestProfile {

    //    @Override
    //    public Map<String, String> getConfigOverrides() {
    //        return Map.of(
    //                // Disable MongoDB Dev Services
    //                "quarkus.mongodb.devservices.enabled", "false",
    //                // Point to dummy host so client fails fast if mistakenly called
    //                "quarkus.mongodb.connection-string", "mongodb://localhost:27018/dummy"
    //        );
    //    }

    //    // Optional: Disable specific Mongo health checks or services
    //    @Override
    //    public Set<String> tags() {
    //        return Set.of("h2-db-only");
    //    }

    @Override
    public String getConfigProfile() {
        // Activates %custom. properties from application.properties / application-custom.properties
        return "customh2db";
    }
}
