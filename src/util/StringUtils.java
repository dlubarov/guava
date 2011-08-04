package util;

public final class StringUtils {
    private StringUtils() {}
    
    public static String implode(String glue, Object[] parts) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object part : parts) {
            if (first)
                first = false;
            else
                sb.append(glue);
            sb.append(part);
        }
        return sb.toString();
    }
    
    public static String implode(char glue, Object[] parts) {
        return implode(Character.toString(glue), parts);
    }

    public static String indent(Object code) {
        String s = code.toString();
        if (s.isEmpty())
            return "";
        String[] lines = s.split("\n");
        for (int i = 0; i < lines.length; ++i)
            lines[i] = "    " + lines[i];
        return implode('\n', lines);
    }
}
