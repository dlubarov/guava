package parse;

import java.util.*;

import parse.misc.*;

import common.NiftyException;

import a.*;
import a.gen.*;

public class TypeDefParser extends Parser<TypeDef> {
    public static final Parser<TypeDef> singleton = new TypeDefParser();
    private TypeDefParser() {}

    @Override
    public Success<TypeDef> parse(String s, int p) {
        // Parse generic constraints.
        Success<GenericConstraint[]> resGenConstraints = GenericConstraintListParser.singleton.parse(s, p);
        p = resGenConstraints.rem;
        p = optWS(s, p);

        // Parse qualifiers and "type" keyword.
        List<String> quals = new ArrayList<String>();
        for (;;) {
            Success<String> resKeyword = IdentifierParser.singleton.parse(s, p);
            if (resKeyword == null)
                throw new NiftyException("Expecting 'type' or a type qualifier.");
            p = resKeyword.rem;
            p = optWS(s, p);
            if (resKeyword.value.equals("type"))
                break;
            quals.add(resKeyword.value);
        }

        // Parse the type name.
        Success<String> resName = IdentifierParser.singleton.parse(s, p);
        p = resName.rem;
        p = optWS(s, p);

        // Parse the generic parameter list.
        List<TypeGenericParam> genericParams = new ArrayList<TypeGenericParam>();
        if (s.charAt(p) == '[') {
            p = optWS(s, p + 1);

            // Parse the first generic parameter name.
            Success<TypeGenericParam> resParam = TypeGenericParamParser.singleton.parse(s, p);
            if (resParam == null) {
                if (s.charAt(p) == ']')
                    throw new NiftyException("Empty generic parameter lists are not accepted.");
                throw new NiftyException("Expecting a generic parameter name after '['.");
            }
            genericParams.add(resParam.value);
            p = resParam.rem;
            p = optWS(s, p);

            // Parse any other generic parameter names.
            for (;;) {
                // Parse the comma.
                if (s.charAt(p) != ',')
                    break;
                p = optWS(s, p + 1);

                // Parse the next generic parameter name.
                resParam = TypeGenericParamParser.singleton.parse(s, p);
                if (resParam == null)
                    throw new NiftyException("Expecting another generic parameter name after ','.");
                genericParams.add(resParam.value);
                p = resParam.rem;
                p = optWS(s, p);
            }

            // Parse the ']'.
            if (s.charAt(p++) != ']')
                throw new NiftyException("Missing closing ']' after generic parameter list.");
            p = optWS(s, p);
        }

        // Parse the "extends" keyword and any parent types following it.
        List<Type> parents = new ArrayList<Type>();
        Success<String> resExtends = IdentifierParser.singleton.parse(s, p);
        if (resExtends != null && resExtends.value.equals("extends")) {
            p = resExtends.rem;
            p = optWS(s, p);

            // Parse first parent (required).
            Success<Type> resParent = TypeParser.singleton.parse(s, p);
            if (resParent == null)
                throw new NiftyException("Expecting parent type after 'extendsd'.");
            parents.add(resParent.value);
            p = resParent.rem;
            p = optWS(s, p);

            for (;;) {
                // Parse comma.
                if (s.charAt(p) != ',')
                    break;
                p = optWS(s, p + 1);

                // Parse next parent.
                resParent = TypeParser.singleton.parse(s, p);
                if (resParent == null)
                    throw new NiftyException("Expecting another parent type after ','.");
                parents.add(resParent.value);
                p = resParent.rem;
                p = optWS(s, p);
            }
        }

        // Parse opening '{'.
        if (s.charAt(p++) != '{')
            throw new NiftyException("Expecting opening '{' after type name.");
        p = optWS(s, p);

        // Parse member definitions.
        List<MemberDef> memberDefs = new ArrayList<MemberDef>();
        for (;;) {
            Success<MemberDef> resMemberDef = MemberDefParser.singleton.parse(s, p);
            if (resMemberDef == null)
                break;
            memberDefs.add(resMemberDef.value);
            p = resMemberDef.rem;
            p = optWS(s, p);
        }

        // Parse closing '}'.
        if (s.charAt(p++) != '}')
            throw new NiftyException("Missing closing '}' for %s's type definition.", resName.value);

        // Format and return the results. (Yuck...)
        String[] qualsArr = quals.toArray(new String[quals.size()]);
        TypeGenericParam[] genericParamsArr = genericParams.toArray(new TypeGenericParam[genericParams.size()]);
        Type[] parentsArr = parents.toArray(new Type[parents.size()]);
        MemberDef[] memberDefsArr = memberDefs.toArray(new MemberDef[memberDefs.size()]);
        TypeDef result = new TypeDef(resGenConstraints.value, qualsArr, resName.value, genericParamsArr, parentsArr, memberDefsArr);
        return new Success<TypeDef>(result, p);
    }
}
