package rst;

import common.*;
import rctx.*;
import vm.*;

import java.util.*;

import static util.StringUtils.*;

public class TypeDef {
    public final RawTypeDesc desc;
    public final GenericInfo[] genericInfos;
    public final NormalFullTypeDesc[] supers;
    public final FieldDef[] staticFields;
    public final FieldDef[] instanceFields;
    public final MethodDef[] methods;
    public final boolean isAbstract, isSealed;

    // TODO: check for cycles in inheritance somewhere

    public TypeDef(RawTypeDesc desc, GenericInfo[] genericInfos, NormalFullTypeDesc[] supers,
                   FieldDef[] staticFields, FieldDef[] instanceFields, MethodDef[] methods,
                   boolean isAbstract, boolean isSealed) {
        this.desc = desc;
        this.genericInfos = genericInfos;
        this.supers = supers;
        this.staticFields = staticFields;
        this.instanceFields = instanceFields;
        this.methods = methods;
        for (MethodDef m : methods)
            m.desc = new RawMethodDesc(desc, m.name, m.numGenericParams, m.paramTypes, m.isStatic);
        this.isAbstract = isAbstract;
        this.isSealed = isSealed;
    }
    
    private NormalFullTypeDesc getFullDesc() {
        FullTypeDesc[] genericArgs = new FullTypeDesc[genericInfos.length];
        for (int i = 0; i < genericArgs.length; ++i)
            genericArgs[i] = new TypeGenericFullTypeDesc(desc, i);
        return new NormalFullTypeDesc(desc, genericArgs);
    }

    public FieldDef getField(String name) {
        for (FieldDef f : instanceFields)
            if (f.name.equals(name))
                return f;
        throw new NoSuchElementException(String.format("%s has no field named \"%s\"", desc, name));
    }

    public int getFieldIndex(String name) {
        for (int i = 0; i < instanceFields.length; ++i)
            if (instanceFields[i].name.equals(name))
                return i;
        throw new NoSuchElementException(String.format("%s has no field named \"%s\"", desc, name));
    }

    public FieldDef getStaticField(String name) {
        for (FieldDef f : staticFields)
            if (f.name.equals(name))
                return f;
        throw new NoSuchElementException(String.format("%s has no static field named \"%s\"", desc, name));
    }

    public int getStaticFieldIndex(String name) {
        for (int i = 0; i < staticFields.length; ++i)
            if (staticFields[i].name.equals(name))
                return i;
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
    public MethodDef getMatchingInstanceMethod(CodeRCtx ctx, String name,
            FullTypeDesc[] methGenericArgs, FullTypeDesc[] argTypes) {
        List<MethodDef> options = new ArrayList<MethodDef>();

        methodsearch:
        for (MethodDef method : methods) {
            if (method.isStatic)
                continue;
            if (!method.name.equals(name))
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
    
    public NormalType compile(GlobalRCtx ctx) {
        RawTypeDesc[] supersRaw = new RawTypeDesc[supers.length];
        for (int i = 0; i < supersRaw.length; ++i)
            supersRaw[i] = supers[i].raw;
        
        Method[] ownedMethods = new Method[methods.length];
        for (int i = 0; i < ownedMethods.length; ++i)
            try {
                ownedMethods[i] = methods[i].compile(new MethodRCtx(ctx, getFullDesc()));
            } catch (Throwable e) {
                throw new RuntimeException("Compilation error in method " + methods[i].desc, e);
            }
        
        // TODO: add inherited fields
        int numFields = instanceFields.length;
        
        Map<RawMethodDesc, RawMethodDesc> vtableDescs = new HashMap<RawMethodDesc, RawMethodDesc>();
        
        return new NormalType(desc, supersRaw,
                ownedMethods, vtableDescs,
                numFields, staticFields.length);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (genericInfos.length > 0) {
            sb.append('[');
            sb.append("]\n");
        }
        sb.append("type ").append(desc);
        if (genericInfos.length > 0) {
            sb.append('[');
            sb.append(genericInfos[0].var).append("T0");
            for (int i = 1; i < genericInfos.length; ++i)
                sb.append(", ").append(genericInfos[i].var).append('T').append(i)
                    .append(" ext ").append(Arrays.toString(genericInfos[i].parentTypes))
                    .append(" sup ").append(Arrays.toString(genericInfos[i].childTypes));
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
