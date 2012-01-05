package a;

import java.util.*;

import util.ArrayUtils;

import common.*;

import a.gen.GenericConstraint;
import a.stm.Block;

public class MethodDef extends MemberDef {
    public final GenericConstraint[] genericConstraints;
    public final String[] quals;
    public final Type returnType;
    public final String name;
    public final String[] genericParams;
    public final Type[] paramTypes;
    public final Block body;

    public MethodDef(GenericConstraint[] genericConstraints, String[] quals,
            Type returnType, String name, String[] genericParams, Type[] paramTypes, Block body) {
        this.genericConstraints = genericConstraints;
        this.quals = quals;
        this.returnType = returnType;
        this.name = name;
        this.genericParams = genericParams;
        this.paramTypes = paramTypes;
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
        if (ArrayUtils.hasDuplicates(quals))
            throw new NiftyException("%s has duplicate modifier", name);

        // Refine generic parameters
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

        // Refine parameter types
        b.Type[] refinedParamTypes = new b.Type[paramTypes.length];
        for (int i = 0; i < refinedParamTypes.length; ++i)
            refinedParamTypes[i] = paramTypes[i].refine();

        return new b.MethodDef(
                visibility(),
                hasQual("static"), hasQual("sealed"),
                returnType.refine(),
                name,
                refinedGenericParams,
                refinedParamTypes,
                body.refine());
    }

    private String qualsString() {
        StringBuilder sb = new StringBuilder();
        for (String q : quals)
            sb.append(q).append(' ');
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format(
                "%s %s %s%s%s %s",
                qualsString(),
                returnType,
                name,
                genericParams.length == 0
                        ? ""
                        : Arrays.toString(genericParams),
                paramTypes,
                body);
    }
}
