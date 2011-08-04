package ast;

import ast.stm.BlockStm;
import ctx.*;

import static util.StringUtils.implode;

public class MethodDef extends MemberDef {
    private final String[] qualifiers;
    public final TypedVar self;
    public final String[] genericParams;
    public final TypedVar[] params;
    private final BlockStm body;

    public MethodDef(String[] qualifiers, TypedVar self, String[] genericParams, TypedVar[] params, BlockStm body) {
        this.qualifiers = qualifiers;
        this.self = self;
        this.genericParams = genericParams;
        this.params = params;
        this.body = body;
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

    public rst.MethodDef refine(MethodContext ctx) {
        Type[] paramTypes = new Type[params.length];
        for (int i = 0; i < paramTypes.length; ++i)
            paramTypes[i] = params[i].type;
        return new rst.MethodDef(self.name, ctx.resolveFull(self.type),
                genericParams.length, ctx.resolveAllFull(paramTypes),
                body == null
                    ? null
                    : (rst.stm.BlockStm) body.refine(new CodeContext(ctx)).stm,
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
