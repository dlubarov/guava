package ast;

import java.util.*;

import rst.GenericInfo;

import common.*;

import ast.exp.LitVoid;
import ast.stm.*;
import ctx.*;

import static util.StringUtils.implode;

public class MethodDef extends MemberDef {
    private final GenericConstraint[] genericConstraints;
    private final String[] qualifiers;
    public final TypedVar self;
    public final String[] genericParams;
    public final TypedVar[] params;
    private final BlockStm body;

    public MethodDef(GenericConstraint[] genericConstraints, String[] qualifiers, TypedVar self,
            String[] genericParams, TypedVar[] params, BlockStm body) {
        this.genericConstraints = genericConstraints;
        this.qualifiers = qualifiers;
        this.self = self;
        this.genericParams = genericParams;
        this.params = params;
        this.body = body;
    }

    private boolean isVoid() {
        String retType = self.type.name;
        return retType.equals("Void") || retType.equals("core.Void");
    }

    private boolean hasQualifier(String qual) {
        for (String q : qualifiers)
            if (q.equals(qual))
                return true;
        return false;
    }

    public boolean isStatic() {
        return hasQualifier("static");
    }

    public boolean isSealed() {
        return hasQualifier("sealed");
    }

    private final FullTypeDesc[] upperBoundsFor(String genericID, MethodContext ctx) {
        List<FullTypeDesc> result = new ArrayList<FullTypeDesc>();
        for (GenericConstraint con : genericConstraints)
            if (con.genericArg.equals(genericID) && con.rel == GenericConstraintRel.SUBTYPE)
                result.add(ctx.resolveFull(con.that));
        if (result.isEmpty())
            result.add(new NormalFullTypeDesc(new RawTypeDesc("core", "Object")));
        return result.toArray(new FullTypeDesc[result.size()]);
    }

    private final FullTypeDesc[] lowerBoundsFor(String genericID, MethodContext ctx) {
        List<FullTypeDesc> result = new ArrayList<FullTypeDesc>();
        for (GenericConstraint con : genericConstraints)
            if (con.genericArg.equals(genericID) && con.rel == GenericConstraintRel.SUPTYPE)
                result.add(ctx.resolveFull(con.that));
        if (result.isEmpty())
            result.add(new NormalFullTypeDesc(new RawTypeDesc("core", "Nothing")));
        return result.toArray(new FullTypeDesc[result.size()]);
    }

    public rst.MethodDef refine(MethodContext ctx) {
        GenericInfo[] genericInfos = new GenericInfo[genericParams.length];
        // TODO: check if any generic names in constraint list are not generic args for this type
        for (int i = 0; i < genericInfos.length; ++i)
            genericInfos[i] = new GenericInfo(
                    Variance.NONVARIANT, // TODO: variance doesn't really apply, should there be a separate class besides GenericInfo?
                    upperBoundsFor(genericParams[i], ctx),
                    lowerBoundsFor(genericParams[i], ctx));

        Type[] paramTypes = new Type[params.length];
        for (int i = 0; i < paramTypes.length; ++i)
            paramTypes[i] = params[i].type;

        rst.stm.BlockStm refBody = null;
        if (body != null) {
            BlockStm tweakedBody = body;
            if (isVoid())
                tweakedBody = new BlockStm(body, new ReturnStm(LitVoid.SINGLETON));
            refBody = (rst.stm.BlockStm) tweakedBody.refine(new CodeContext(ctx)).stm;
        }

        return new rst.MethodDef(self.name, ctx.resolveFull(self.type),
                genericParams.length, ctx.resolveAllFull(paramTypes),
                refBody,
                isStatic(),
                isSealed());
    }

    public int genericParamIdx(String typeName) {
        for (int i = 0; i < genericParams.length; ++i)
            if (genericParams[i].equals(typeName))
                return i;
        throw new IllegalArgumentException();
    }

    public boolean isGenericParam(String typeName) {
        try {
            genericParamIdx(typeName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (genericConstraints.length != 0)
            sb.append(Arrays.toString(genericConstraints)).append('\n');
        for (String q : qualifiers)
            sb.append(q).append(' ');
        sb.append(self);
        if (genericParams.length > 0)
            sb.append('[').append(implode(", ", genericParams)).append(']');
        sb.append('(').append(implode(", ", params)).append(')');
        if (body == null)
            sb.append(';');
        else
            sb.append(' ').append(body);
        return sb.toString();
    }
}
