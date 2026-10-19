package br.com.daniel.java.quarkus.general.utils;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class BaseCaracteresUtils {

    private String baseCharacters;

    public BaseCaracteresUtils() {
        baseCharacters = StringUtils.EMPTY;
        for (char i = '0'; i <= '9'; i++) baseCharacters += i;
        for (char i = 'a'; i <= 'z'; i++) baseCharacters += i;
        for (char i = 'A'; i <= 'Z'; i++) baseCharacters += i;
    }
}
