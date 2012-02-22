package util;

import java.util.Collection;

public final class StringUtils {
    private StringUtils() {}

    public static String implode(String glue, Object[] parts) {
        StringBuilder sb = new StringBuilder();
        for (Object part : parts) {
            if (sb.length() > 0)
                sb.append(glue);
            sb.append(part);
        }
        return sb.toString();
    }

    public static String implode(char glue, Object[] parts) {
        return implode(Character.toString(glue), parts);
    }

    public static String implode(String glue, Collection<?> parts) {
        return implode(glue, parts.toArray());
    }

    public static String implode(char glue, Collection<?> parts) {
        return implode(Character.toString(glue), parts);
    }

    public static String indent(Object code, int amount) {
        String s = code.toString();
        if (s.isEmpty())
            return "";
        String[] lines = s.split("\n");
        for (int i = 0; i < lines.length; ++i)
            for (int j = 0; j < amount; ++j)
                lines[i] = "    " + lines[i];
        return implode('\n', lines);
    }

    public static String indent(Object code) {
        return indent(code, 1);
    }

    public static boolean containsAt(String s, String substr, int p) {
        try {
            return s.substring(p, p + substr.length()).equals(substr);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
}
