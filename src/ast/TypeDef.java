package ast;

import common.*;
import ctx.*;

import java.util.*;

import static util.StringUtils.*;

public class TypeDef {
    private final String[] quals;
    public final String name;
    public final GenericParamDec[] genericParams;
    public final Type[] supers;
    public final MemberDef[] members;
    
    public TypeDef(String[] quals, String name, GenericParamDec[] genericParams, Type[] supers, MemberDef[] members) {
        this.quals = quals;
        this.name = name;
        this.genericParams = genericParams;
        this.supers = supers;
        this.members = members;
    }

    private boolean hasQualifier(String qual) {
        for (String q : quals)
            if (q.equals(qual))
                return true;
        return false;
    }

    public boolean isAbstract() {
        return hasQualifier("abstract");
    }

    public boolean isSealed() {
        return hasQualifier("sealed");
    }

    public rst.TypeDef refine(TypeContext ctx) {
        Variance[] variances = new Variance[genericParams.length];
        for (int i = 0; i < variances.length; ++i)
            variances[i] = genericParams[i].var;
        
        List<rst.FieldDef> staticFields = new ArrayList<rst.FieldDef>();
        List<rst.FieldDef> instanceFields = new ArrayList<rst.FieldDef>();
        List<rst.MethodDef> methods = new ArrayList<rst.MethodDef>();
        for (MemberDef mem : members) {
            if (mem instanceof FieldDef) {
                FieldDef field = (FieldDef) mem;
                if (field.isStatic())
                    staticFields.add(field.refine(ctx));
                else
                    instanceFields.add(field.refine(ctx));
            } else if (mem instanceof MethodDef)
                methods.add(((MethodDef) mem).refine(new MethodContext(ctx, (MethodDef) mem)));
            else assert false;
        }

        String module = ctx.desc().module;
        NormalFullTypeDesc[] supersRef;
        if (module.equals("core") && name.equals("Object"))
            supersRef = new NormalFullTypeDesc[0];
        else if (supers.length == 0)
            supersRef = new NormalFullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))};
        else {
            FullTypeDesc[] supersRaw = ctx.resolveAllFull(supers);
            supersRef = new NormalFullTypeDesc[supersRaw.length];
            for (int i = 0; i < supersRaw.length; ++i)
                try {
                    supersRef[i] = (NormalFullTypeDesc) supersRaw[i];
                } catch (ClassCastException e) {
                    throw new RuntimeException(String.format("why would you want to extend %s? preposterous!", supersRaw[i]));
                }
        }
        
        return new rst.TypeDef(
                new RawTypeDesc(module, name),
                variances,
                supersRef,
                staticFields.toArray(new rst.FieldDef[staticFields.size()]),
                instanceFields.toArray(new rst.FieldDef[instanceFields.size()]),
                methods.toArray(new rst.MethodDef[methods.size()]),
                isAbstract(),
                isSealed());
    }

    public int genericParamIdx(String typeName) {
        for (int i = 0; i < genericParams.length; ++i)
            if (genericParams[i].name.equals(typeName))
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
        for (String q : quals)
            sb.append(q).append(' ');
        sb.append("type ").append(name);
        if (genericParams.length > 0)
            sb.append('[').append(implode(", ", genericParams)).append(']');
        if (supers.length > 0)
            sb.append(" extends ").append(implode(", ", supers));
        sb.append(" {");
        if (members.length > 0)
            sb.append('\n').append(indent(implode("\n\n", members))).append('\n');
        return sb.append('}').toString();
    }
}
