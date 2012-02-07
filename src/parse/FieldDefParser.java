package parse;

import java.util.*;

import common.NiftyException;

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

        // Parse the first field name.
        List<String> names = new ArrayList<String>();
        Success<String> resName = IdentifierParser.singleton.parse(s, p);
        if (resName == null)
            return null;
        names.add(resName.value);
        p = resName.rem;
        p = optWS(s, p);

        // Parse any additional field names.
        for (;;) {
            // Parse the comma.
            if (s.charAt(p) != ',')
                break;
            p = optWS(s, p + 1);

            // Parse the next field name.
            resName = IdentifierParser.singleton.parse(s, p);
            if (resName == null)
                throw new NiftyException("Expecting another field name after ','.");
            names.add(resName.value);
            p = resName.rem;
            p = optWS(s, p);
        }

        // Parse the semicolon.
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
