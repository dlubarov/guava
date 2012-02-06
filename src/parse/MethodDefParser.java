package parse;

import java.util.*;

import common.NiftyException;

import parse.misc.*;
import parse.stm.BlockParser;
import a.*;
import a.gen.GenericConstraint;
import a.stm.Block;

public class MethodDefParser extends Parser<MemberDef> {
    public static final Parser<MemberDef> singleton = new MethodDefParser();
    private MethodDefParser() {}

    private static String[] methodQualifiers = {"public", "private", "static", "sealed"};

    private static boolean isMethodQualifier(String s) {
        for (String qual : methodQualifiers)
            if (s.equals(qual))
                return true;
        return false;
    }

    private static Success<String> parseMethodQualifier(String s, int p) {
        Success<String> resQual = IdentifierParser.singleton.parse(s, p);
        if (resQual != null && isMethodQualifier(resQual.value))
            return resQual;
        return null;
    }

    @Override
    public Success<MemberDef> parse(String s, int p) {
        // Parse generic constraints.
        Success<GenericConstraint[]> resGenConstraints = GenericConstraintListParser.singleton.parse(s, p);
        p = resGenConstraints.rem;
        p = optWS(s, p);

        // Parse any qualifiers.
        List<String> quals = new ArrayList<String>();
        for (;;) {
            Success<String> resQual = parseMethodQualifier(s, p);
            if (resQual == null)
                break;
            quals.add(resQual.value);
            p = resQual.rem;
            p = optWS(s, p);
        }

        // Parse the return type.
        Success<Type> resType = TypeParser.singleton.parse(s, p);
        if (resType == null)
            return null;
        p = resType.rem;
        p = optWS(s, p);

        // Parse the method name.
        Success<String> resName = IdentifierParser.singleton.parse(s, p);
        if (resName == null)
            return null;
        p = resName.rem;
        p = optWS(s, p);

        // Parse the generic parameter list, if there is one.
        List<String> genericParams = new ArrayList<String>();
        if (s.charAt(p) == '[') {
            p = optWS(s, p + 1);
            for (;;) {
                Success<String> resGenParam = IdentifierParser.singleton.parse(s, p);
                if (resGenParam == null)
                    break;
                genericParams.add(resGenParam.value);
                p = resGenParam.rem;
                p = optWS(s, p);
            }
            if (s.charAt(p++) != ']')
                throw new NiftyException("Unclosed generic param list; expecting ']'.");
            if (genericParams.isEmpty())
                throw new NiftyException("Found empty list of generic params.");
            p = optWS(s, p);
        }

        // Parse the parameter list.
        List<Type> paramTypes = new ArrayList<Type>();
        List<String> paramNames = new ArrayList<String>();
        if (s.charAt(p++) != '(')
            return null;
        p = optWS(s, p);
        for (;;) {
            Success<Type> resParamType = TypeParser.singleton.parse(s, p);
            if (resParamType == null)
                break;
            p = resParamType.rem;
            p = optWS(s, p);
            Success<String> resParamName = IdentifierParser.singleton.parse(s, p);
            if (resParamName == null)
                throw new NiftyException("Found parameter type with no corresponding name.");
            p = resParamName.rem;
            p = optWS(s, p);
            paramTypes.add(resParamType.value);
            paramNames.add(resParamName.value);
        }
        if (s.charAt(p++) != ')')
            throw new NiftyException("Expecting ')' to close method parameter list.");
        p = optWS(s, p);

        // Parse the method body.
        Block body;
        if (s.charAt(p) == ';') {
            // This is an abstract method.
            body = null;
            ++p;
        } else {
            Success<Block> resBody = BlockParser.singleton.parse(s, p);
            if (resBody == null)
                throw new NiftyException("Expecting method body or ';'.");
            body = resBody.value;
            p = resBody.rem;
        }
        p = optWS(s, p);

        // Collect the results.
        String[] qualsArr = quals.toArray(new String[quals.size()]);
        String[] genericParamsArr = genericParams.toArray(new String[genericParams.size()]);
        Type[] paramTypesArr = paramTypes.toArray(new Type[paramTypes.size()]);
        String[] paramNamesArr = paramNames.toArray(new String[paramNames.size()]);
        MethodDef result = new MethodDef(
                resGenConstraints.value, qualsArr, resType.value, resName.value,
                genericParamsArr, paramTypesArr, paramNamesArr, body);
        return new Success<MemberDef>(result, p);
    }
}
