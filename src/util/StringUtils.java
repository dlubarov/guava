package util;

import java.util.*;

public final class StringUtils {
    private StringUtils() {}

    public static Comparator<String> descendingLengthComparator = new Comparator<String>() {
        @Override
        public int compare(String a, String b) {
            int diff = b.length() - a.length();
            if (diff == 0)
                return a.compareTo(b);
            return diff;
        }
    };

    public static void sortDescendingLength(String[] strings) {
        Arrays.sort(strings, descendingLengthComparator);
    }

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

    public static char unescape(char c) {
        switch (c) {
            case 'r': return '\r';
            case 'n': return '\n';
            case 't': return '\t';
            case '0': return '\0';
            case '\'': return '\'';
            case '"': return '"';
            case '\\': return '\\';
            default:
                throw new IllegalArgumentException("Invalid escape sequence: \\" + c);
        }
    }

    public static boolean containsAt(String s, String substr, int p) {
        try {
            return s.substring(p, p + substr.length()).equals(substr);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
}
