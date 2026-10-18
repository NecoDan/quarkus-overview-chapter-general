package br.com.daniel.java.quarkus.general.config;

import br.com.daniel.java.quarkus.general.utils.FunctionalUtils;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jasypt.util.text.StrongTextEncryptor;

import java.util.UUID;

public class EncryptoManagerConfig {

    private static final StrongTextEncryptor encryptor;
    private static final String PROFILE_NAME_TEST = "test";

    private EncryptoManagerConfig() {
        throw new IllegalStateException("This is a utility class EncryptoManagerConfig and cannot be instantiated");
    }

    static {
        encryptor = new StrongTextEncryptor();
        encryptor.setPassword(getPassword());
    }

    private static String getPassword() {
        if (isProfileTest()) {
            return UUID.randomUUID().toString();
        }

        return generateValueConfigProviderPasswor("APP_KEY_PWD_ENCRYPT");
    }

    private static String generateValueConfigProviderPasswor(String key) {
        return ConfigProvider.getConfig().getValue(key, String.class);
    }

    private static boolean isProfileTest() {
        return FunctionalUtils.getActiveProfiles().stream().anyMatch(s -> s.equals(PROFILE_NAME_TEST));
    }

    public static String encrypt(String rawText) {
        return encryptor.encrypt(rawText);
    }

    public static String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }
}
