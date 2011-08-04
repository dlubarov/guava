package rst;

import common.*;
import rctx.CodeRCtx;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static util.StringUtils.*;

public class TypeDef {
    public final RawTypeDesc desc;
    public final Variance[] genericParamVars;
    public final NormalFullTypeDesc[] supers;
    public final FieldDef[] staticFields;
    public final FieldDef[] instanceFields;
    public final MethodDef[] methods;
    public final boolean isAbstract, isSealed;

    // TODO: check for cycles in inheritance somewhere

    public TypeDef(RawTypeDesc desc, Variance[] genericParamVars, NormalFullTypeDesc[] supers,
                   FieldDef[] staticFields, FieldDef[] instanceFields, MethodDef[] methods,
                   boolean isAbstract, boolean isSealed) {
        this.desc = desc;
        this.genericParamVars = genericParamVars;
        this.supers = supers;
        this.staticFields = staticFields;
        this.instanceFields = instanceFields;
        this.methods = methods;
        this.isAbstract = isAbstract;
        this.isSealed = isSealed;
    }

    public FieldDef getField(String name) {
        for (FieldDef f : instanceFields)
            if (f.name.equals(name))
                return f;
        throw new NoSuchElementException(String.format("%s has no field named \"%s\"", desc, name));
    }

    public FieldDef getStaticField(String name) {
        for (FieldDef f : staticFields)
            if (f.name.equals(name))
                return f;
        throw new NoSuchElementException(String.format("%s has no static field named \"%s\"", desc, name));
    }

    // TODO: this will need to take type-level generic args to handle code like
    // type SomeType[T] {
    //     <T extends String>
    //     foo() {...}
    //
    //     <T extends Int>
    //     foo() {...}
    // }
    public MethodDef getMatchingInstanceMethod(CodeRCtx ctx, String name, FullTypeDesc[] methGenericArgs, FullTypeDesc[] argTypes) {
        List<MethodDef> options = new ArrayList<MethodDef>();

        methodsearch:
        for (MethodDef method : methods) {
            if (method.isStatic)
                continue;
            if (method.numGenericParams != methGenericArgs.length)
                continue;
            if (method.paramTypes.length != argTypes.length)
                continue;

            for (int i = 0; i < argTypes.length; ++i)
                if (!argTypes[i].isSubtype(method.paramTypes[i], ctx))
                    continue methodsearch;

            options.add(method);
        }

        // If this type doesn't own a matching method, try its supertypes
        if (options.isEmpty())
            for (NormalFullTypeDesc superDesc : supers) {
                TypeDef superType = ctx.resolve(superDesc.raw);
                options.add(superType.getMatchingInstanceMethod(ctx, name, methGenericArgs, argTypes));
            }
        
        if (options.isEmpty())
            throw new NoSuchElementException(String.format("%s.%s", desc, name));
        if (options.size() > 1)
            throw new RuntimeException(String.format("ambiguous method call: %s.%s", desc, name));
        return options.get(0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("type ").append(desc);
        if (genericParamVars.length > 0) {
            sb.append('[');
            sb.append(genericParamVars[0]).append("T0");
            for (int i = 1; i < genericParamVars.length; ++i)
                sb.append(", ").append(genericParamVars[i]).append('T').append(i);
            sb.append(']');
        }
        sb.append(" extends ");
        for (int i = 0; i < supers.length; ++i) {
            if (i > 0)
                sb.append(", ");
            sb.append(supers[i]);
        }
        sb.append(" {");
        if (staticFields.length + instanceFields.length + methods.length > 0) {
            sb.append('\n').append(indent(implode('\n', staticFields)));
            if (staticFields.length > 0 && instanceFields.length > 0)
                sb.append("\n\n");
            sb.append(indent(implode('\n', instanceFields)));
            if (instanceFields.length > 0 && methods.length > 0)
                sb.append("\n\n");
            sb.append(indent(implode("\n\n", methods))).append('\n');
        }
        return sb.append('}').toString();
    }
}
