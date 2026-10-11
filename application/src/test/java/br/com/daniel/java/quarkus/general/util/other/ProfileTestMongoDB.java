//package br.com.daniel.java.quarkus.general.util.other;
//
//import io.quarkus.test.junit.QuarkusTestProfile;
//
//import java.util.Map;
//
//public class ProfileTestMongoDB implements QuarkusTestProfile {
//
//    //    @Override
//    //    public Map<String, String> getConfigOverrides() {
//    //        return Map.of(
//    //                "quarkus.mongodb.devservices.image-name", "mongo:7.0",
//    //                "quarkus.mongodb.connection-string", "mongodb://localhost:27018",
//    //                "quarkus.mongodb.database", "db_btgpactualtest",
//    //                "quarkus.mongodb.credentials.username", "admin",
//    //                "quarkus.mongodb.credentials.password", "admintest123",
//    //                "quarkus.mongodb.credentials.auth-source", "admin",
//    //                // Change the Dev Services port to avoid conflicts
//    //                "quarkus.mongodb.devservices.port", "27018",
//    //                // Or disable Dev Services entirely for this test suite
//    //                // "quarkus.mongodb.devservices.enabled", "false",
//    //                "quarkus.log.category.\"org.acme\".level", "INFO",
//    //                "quarkus.mongodb.devservices.enabled", "false"
//    //        );
//    //    }
//
//    @Override
//    public Map<String, String> getConfigOverrides() {
//        return Map.of(
//                "quarkus.mongodb.devservices.enabled", "false",
//                "quarkus.mongodb.connection-string", "mongodb://localhost:27017"
//        );
//    }
//
//    // Optional: Set a named Quarkus profile (activates application-%profile%.properties)
//    @Override
//    public String getConfigProfile() {
//        return "test-mongodb";
//    }
//}
