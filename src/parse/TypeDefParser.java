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
        List<GenericConstraint> genericConstraints = new ArrayList<GenericConstraint>();
        if (s.charAt(p) == '[') {
            p = optWS(s, ++p);
            for (;;) {
                Success<GenericConstraint> resConstraint = GenericConstraintParser.singleton.parse(s, p);
                if (resConstraint == null)
                    break;
                genericConstraints.add(resConstraint.value);
                p = resConstraint.rem;
                p = optWS(s, p);
            }
            if (s.charAt(p++) != ']')
                throw new NiftyException("Missing closing ']' after generic constraint list.");
            if (genericConstraints.isEmpty())
                throw new NiftyException("List of generic constraints is empty.");
            p = optWS(s, p);
        }

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
            p = optWS(s, ++p);
            for (;;) {
                Success<TypeGenericParam> resParam = TypeGenericParamParser.singleton.parse(s, p);
                if (resParam == null)
                    break;
                genericParams.add(resParam.value);
                p = resParam.rem;
                p = optWS(s, p);
            }
            if (s.charAt(p++) != ']')
                throw new NiftyException("Missing closing ']' after generic parameter list.");
            if (genericParams.isEmpty())
                throw new NiftyException("No generic parameters found between the square brackets.");
            p = optWS(s, p);
        }

        // Parse the "extends" keyword and any parent types following it.
        List<Type> parents = new ArrayList<Type>();
        Success<String> resExtends = IdentifierParser.singleton.parse(s, p);
        if (resExtends != null && resExtends.value.equals("extends")) {
            p = resExtends.rem;
            p = optWS(s, p);
            for (;;) {
                Success<Type> resParent = TypeParser.singleton.parse(s, p);
                if (resParent == null)
                    break;
                parents.add(resParent.value);
                p = resParent.rem;
                p = optWS(s, p);
            }
            if (parents.isEmpty())
                throw new NiftyException("No types found after 'extends'.");
        }

        // Parse opening '{'.
        if (s.charAt(p++) != '{')
            throw new NiftyException("Expecting opening '{' after type name.");

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
            throw new NiftyException("Missing closing '}' for type definition.");

        // Format and return the results. (Yuck...)
        GenericConstraint[] genericConstraintsArr = genericConstraints.toArray(new GenericConstraint[genericConstraints.size()]);
        String[] qualsArr = quals.toArray(new String[quals.size()]);
        TypeGenericParam[] genericParamsArr = genericParams.toArray(new TypeGenericParam[genericParams.size()]);
        Type[] parentsArr = parents.toArray(new Type[parents.size()]);
        MemberDef[] memberDefsArr = memberDefs.toArray(new MemberDef[memberDefs.size()]);
        TypeDef result = new TypeDef(genericConstraintsArr, qualsArr, resName.value, genericParamsArr, parentsArr, memberDefsArr);
        return new Success<TypeDef>(result, p);
    }
}
