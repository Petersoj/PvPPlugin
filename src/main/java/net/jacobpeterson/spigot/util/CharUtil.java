package net.jacobpeterson.spigot.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class CharUtil {

    // Strictly for reference in fromUTFHexCode
    private static final Charset UTF_8_CHARSET = StandardCharsets.UTF_8;

    public static final String DOUBLE_RIGHT_ARROW = fromUTFHexCode("\u00BB");

    private static String fromUTFHexCode(String code) {
        return new String(code.getBytes(UTF_8_CHARSET), UTF_8_CHARSET);
    }

}
