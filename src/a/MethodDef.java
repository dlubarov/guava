package a;

import java.util.*;

import util.ArrayUtils;

import common.*;

import a.gen.GenericConstraint;
import a.stm.*;

public class MethodDef extends MemberDef {
    public final GenericConstraint[] genericConstraints;
    public final String[] quals;
    public final Type returnType;
    public final String name;
    public final String[] genericParams;
    public final Type[] paramTypes;
    public final String[] paramNames;
    public final Block body;

    public MethodDef(GenericConstraint[] genericConstraints, String[] quals,
            Type returnType, String name,
            String[] genericParams, Type[] paramTypes, String[] paramNames,
            Block body) {
        this.genericConstraints = genericConstraints;
        this.quals = quals;
        this.returnType = returnType;
        this.name = name;
        this.genericParams = genericParams;
        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
        this.body = body;
    }

    public boolean hasQual(String qual) {
        for (String q : quals)
            if (q.equals(qual))
                return true;
        return false;
    }

    private MethodVisibility visibility() {
        MethodVisibility result = null;
        if (hasQual("public"))
            result = MethodVisibility.PUBLIC;
        if (hasQual("private")) {
            if (result != null)
                throw new RuntimeException("conflicting visibility qualifiers");
            result = MethodVisibility.PRIVATE;
        }
        if (result == null)
            return MethodVisibility.MODULE;
        return result;
    }

    public b.MethodDef refine() {
        // Validity checks.
        if (ArrayUtils.hasDuplicates(quals))
            throw new NiftyException("%s has duplicate modifier", name);
        // TODO: check for invalid qualifiers.

        // Refine generic parameters.
        b.gen.MethodGenericParam[] refinedGenericParams = new b.gen.MethodGenericParam[genericParams.length];
        for (int i = 0; i < refinedGenericParams.length; ++i) {
            Set<b.Type> subOf = new HashSet<b.Type>(), supOf = new HashSet<b.Type>();
            for (GenericConstraint constraint : genericConstraints)
                if (constraint.genericParam.equals(genericParams[i]))
                    switch (constraint.rel) {
                        case SUBTYPE:
                            subOf.add(constraint.otherType.refine());
                            break;
                        case SUPERTYPE:
                            supOf.add(constraint.otherType.refine());
                            break;
                    }
            if (subOf.isEmpty())
                subOf.add(new b.Type("Top"));
            if (supOf.isEmpty())
                supOf.add(new b.Type("Bottom"));
            refinedGenericParams[i] = new b.gen.MethodGenericParam(
                    genericParams[i],
                    subOf.toArray(new b.Type[subOf.size()]),
                    supOf.toArray(new b.Type[supOf.size()]));
        }

        // Refine parameter types.
        b.Type[] refinedParamTypes = Type.refineAll(paramTypes);

        // Refine method body.
        b.stm.Block refinedBody;
        if (body == null)
            refinedBody = null;
        else {
            Block tweakedBody = body;
            if (returnType.equals(new Type("Unit")))
                tweakedBody = new Block(tweakedBody, new Return(null));
            refinedBody = tweakedBody.refine();
        }

        return new b.MethodDef(
                visibility(),
                hasQual("static"), hasQual("sealed"),
                returnType.refine(),
                name,
                refinedGenericParams,
                refinedParamTypes,
                paramNames,
                refinedBody);
    }

    private String qualsString() {
        StringBuilder sb = new StringBuilder();
        for (String q : quals)
            sb.append(q).append(' ');
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(qualsString());
        sb.append(returnType).append(' ').append(name);

        // Append the generic parameter list.
        if (genericParams.length > 0)
            sb.append(Arrays.toString(genericParams));

        // Append the parameter list.
        sb.append('(');
        for (int i = 0; i < paramNames.length; ++i) {
            if (i > 0)
                sb.append(", ");
            sb.append(paramTypes[i]).append(' ').append(paramNames[i]);
        }
        sb.append(')');

        // Append the body.
        if (body == null)
            sb.append(';');
        else
            sb.append(' ').append(body);

        return sb.toString();
    }
}
