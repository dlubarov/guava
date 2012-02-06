package parse;

import java.util.*;

import parse.misc.*;

import a.*;

public class FieldDefParser extends Parser<MemberDef> {
    public static final Parser<MemberDef> singleton = new FieldDefParser();
    private FieldDefParser() {}

    private static String[] fieldQualifiers = {"public", "private", "static", "readonly"};

    private static boolean isFieldQualifier(String s) {
        for (String qual : fieldQualifiers)
            if (s.equals(qual))
                return true;
        return false;
    }

    private static Success<String> parseFieldQualifier(String s, int p) {
        Success<String> resQual = IdentifierParser.singleton.parse(s, p);
        if (resQual != null && isFieldQualifier(resQual.value))
            return resQual;
        return null;
    }

    @Override
    public Success<MemberDef> parse(String s, int p) {
        // Parse any qualifiers.
        List<String> quals = new ArrayList<String>();
        for (;;) {
            Success<String> resQual = parseFieldQualifier(s, p);
            if (resQual == null)
                break;
            quals.add(resQual.value);
            p = resQual.rem;
            p = optWS(s, p);
        }

        // Parse the type.
        Success<Type> resType = TypeParser.singleton.parse(s, p);
        if (resType == null)
            return null;
        p = resType.rem;
        p = optWS(s, p);

        // Parse the field names.
        List<String> names = new ArrayList<String>();
        for (;;) {
            Success<String> resName = IdentifierParser.singleton.parse(s, p);
            if (resName == null)
                break;
            names.add(resName.value);
            p = resName.rem;
            p = optWS(s, p);
        }
        if (names.isEmpty())
            return null;
        p = optWS(s, p);
        if (s.charAt(p++) != ';')
            return null;

        // Collect and return the results.
        String[] qualsArr = quals.toArray(new String[quals.size()]);
        String[] namesArr = names.toArray(new String[names.size()]);
        FieldDef result = new FieldDef(qualsArr, resType.value, namesArr);
        return new Success<MemberDef>(result, p);
    }
}
