package rst;

import common.*;
import rctx.*;
import util.StringUtils;
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
            FullTypeDesc[] typeGenericArgs, FullTypeDesc[] methGenericArgs, FullTypeDesc[] argTypes) {
        Set<MethodDef> options = new HashSet<MethodDef>();

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

            for (int i = 0; i < argTypes.length; ++i) {
                FullTypeDesc paramType = method.paramTypes[i];
                paramType = paramType.withGenerics(typeGenericArgs, methGenericArgs);
                if (!argTypes[i].isSubtype(paramType, ctx.methodCtx.globalCtx))
                    continue methodsearch;
            }

            options.add(method);
        }

        // If this type doesn't own a matching method, try its supertypes
        if (options.isEmpty())
            for (NormalFullTypeDesc superDesc : supers) {
                TypeDef superType = ctx.resolve(superDesc.raw);
                // FIXME: need to find where method was originally declared, e.g. if two supertypes override Object.hashCode
                try {
                    options.add(superType.getMatchingInstanceMethod(ctx, name, typeGenericArgs, methGenericArgs, argTypes));
                } catch (NoSuchElementException e) {}
            }

        if (options.isEmpty())
            throw new NoSuchElementException(String.format("%s.%s(%s)", desc, name, StringUtils.implode(", ", argTypes)));
        if (options.size() > 1)
            throw new RuntimeException(String.format("ambiguous method call: %s.%s", desc, name));

        for (MethodDef m : options)
            return m;
        throw new RuntimeException("can't get here");
    }

    protected Set<RawMethodDesc> findAllInstanceMethods(GlobalRCtx ctx) {
        Set<RawMethodDesc> result = new HashSet<RawMethodDesc>();
        for (MethodDef m : methods)
            if (!m.isStatic)
                result.add(m.desc);
        for (NormalFullTypeDesc sup : supers) {
            TypeDef supType = ctx.resolve(sup.raw);
            Set<RawMethodDesc> supMeths = supType.findAllInstanceMethods(ctx);
            for (RawMethodDesc m : supMeths) {
                m = m.withTypeGenerics(sup.genericArgs);
                result.add(m);
                result.add(new RawMethodDesc(desc, m.name, m.numGenericParams, m.paramTypes, false));
            }
        }
        return result;
    }

    protected RawMethodDesc myImplementationOf(GlobalRCtx ctx, RawMethodDesc method, FullTypeDesc[] myGenericArgs) {
        // If I implement this method myself, return my implementation
        for (MethodDef m : methods) {
            if (m.desc.withTypeGenerics(myGenericArgs).canOverride(ctx, method)) {
                if (m.body == null) {
                    if (!isAbstract)
                        throw new RuntimeException("no implementation of " + method);
                } else
                    return m.desc;
            }
        }
        // Otherwise, check my supertypes for an implementation
        List<RawMethodDesc> impls = new ArrayList<RawMethodDesc>();
        for (NormalFullTypeDesc sup : supers) {
            FullTypeDesc[] newGenericArgs = new FullTypeDesc[sup.genericArgs.length];
            for (int i = 0; i < newGenericArgs.length; ++i)
                newGenericArgs[i] = sup.genericArgs[i].withTypeGenerics(myGenericArgs);
            RawMethodDesc impl = ctx.resolve(sup.raw).myImplementationOf(ctx, method, newGenericArgs);
            if (impl != null) impls.add(impl);
        }
        if (impls.isEmpty())
            return null;
        if (impls.size() > 1)
            throw new RuntimeException(String.format("%s inherits multiple implementations of %s", desc, method));
        return impls.get(0);
    }

    protected RawMethodDesc myImplementationOf(GlobalRCtx ctx, RawMethodDesc method) {
        FullTypeDesc[] myGenericArgs = new FullTypeDesc[genericInfos.length];
        for (int i = 0; i < myGenericArgs.length; ++i)
            myGenericArgs[i] = new TypeGenericFullTypeDesc(desc, i);
        return myImplementationOf(ctx, method, myGenericArgs);
    }

    public Type compile(GlobalRCtx ctx) {
        RawTypeDesc[] supersRaw = new RawTypeDesc[supers.length];
        for (int i = 0; i < supersRaw.length; ++i)
            supersRaw[i] = supers[i].raw;

        // TODO: add inherited fields
        int numFields = instanceFields.length;

        Map<RawMethodDesc, RawMethodDesc> vtableDescs = null;
        if (!isAbstract) {
            vtableDescs = new HashMap<RawMethodDesc, RawMethodDesc>();
            Set<RawMethodDesc> allMyMethods = findAllInstanceMethods(ctx);
            for (RawMethodDesc m : allMyMethods) {
                RawMethodDesc impl = myImplementationOf(ctx, m);
                if (impl == null)
                    throw new RuntimeException(String.format("%s does not implement %s", desc, m));
                else
                    vtableDescs.put(m, impl);
            }
        }

        for (NativeType type : God.nativeTypes())
            if (type.desc.equals(desc)) {
                // TODO: update native type to include non-empty methods, vtable
                type.vtableDescs = vtableDescs;
                return type;
            }

        Method[] ownedMethods = new Method[methods.length];
        for (int i = 0; i < ownedMethods.length; ++i)
            try {
                ownedMethods[i] = methods[i].compile(new MethodRCtx(ctx, getFullDesc()));
            } catch (Throwable e) {
                throw new RuntimeException("Compilation error in method " + methods[i].desc, e);
            }

        return new NormalType(desc, supersRaw,
                ownedMethods, vtableDescs,
                numFields, staticFields.length);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (genericInfos.length > 0) {
            sb.append('[');
            // TODO: finish toString
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
            // TODO: this adds no space between static fields and method defs
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
