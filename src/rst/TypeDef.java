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

    public FullTypeDesc inMyGenerics(TypeGenericFullTypeDesc tgDesc, FullTypeDesc[] myGenerics, GlobalRCtx ctx) {
        if (tgDesc.owner.equals(desc))
            return myGenerics[tgDesc.index];
        for (NormalFullTypeDesc sup : supers) {
            FullTypeDesc[] supGenericArgs = new FullTypeDesc[sup.genericArgs.length];
            for (int i = 0; i < supGenericArgs.length; ++i)
                supGenericArgs[i] = sup.genericArgs[i].withTypeGenerics(myGenerics);
            FullTypeDesc result = ctx.resolve(sup.raw).inMyGenerics(tgDesc, supGenericArgs, ctx);
            if (result != null)
                return result;
        }
        return null;
    }

    private Set<RawMethodDesc> findAllInstanceMethods(GlobalRCtx ctx) {
        Set<RawMethodDesc> result = new HashSet<RawMethodDesc>();
        for (MethodDef m : methods)
            if (!m.isStatic)
                result.add(m.desc);
        for (NormalFullTypeDesc sup : supers) {
            TypeDef supType = ctx.resolve(sup.raw);
            Set<RawMethodDesc> supMeths = supType.findAllInstanceMethods(ctx);
            for (RawMethodDesc m : supMeths)
                result.add(m);
        }
        return result;
    }

    public FullTypeDesc inMyGenerics(TypeGenericFullTypeDesc tgDesc, GlobalRCtx ctx) {
        FullTypeDesc[] myGenerics = new FullTypeDesc[genericInfos.length];
        for (int i = 0; i < myGenerics.length; ++i)
            myGenerics[i] = new TypeGenericFullTypeDesc(desc, i);
        return inMyGenerics(tgDesc, myGenerics, ctx);
    }

    public RawMethodDesc inMyGenerics(RawMethodDesc m, GlobalRCtx ctx) {
        TypeDef ownerType = ctx.resolve(m.owner);
        FullTypeDesc[] newGenericArgs = new FullTypeDesc[ownerType.genericInfos.length];
        for (int i = 0; i < newGenericArgs.length; ++i)
            newGenericArgs[i] = inMyGenerics(new TypeGenericFullTypeDesc(m.owner, i), ctx);
        return m.withTypeGenerics(newGenericArgs);
    }

    private Map<RawMethodDesc, RawMethodDesc> cachedTable = null;

    private Map<RawMethodDesc, RawMethodDesc> vtable(GlobalRCtx ctx) {
        if (cachedTable != null)
            return cachedTable;

        Set<RawMethodDesc> allMethods = findAllInstanceMethods(ctx);
        Map<RawMethodDesc, RawMethodDesc> result = new HashMap<RawMethodDesc, RawMethodDesc>();
        for (RawMethodDesc meth : allMethods) {
            RawMethodDesc methMyGen = inMyGenerics(meth, ctx);
            List<RawMethodDesc> options = new ArrayList<RawMethodDesc>();
            for (MethodDef m : methods)
                if (m.desc.canOverride(ctx, methMyGen))
                    options.add(m.body == null? null : m.desc);
            if (options.isEmpty())
                for (NormalFullTypeDesc sup : supers) {
                    Map<RawMethodDesc, RawMethodDesc> supTable = ctx.resolve(sup.raw).vtable(ctx);
                    RawMethodDesc impl = supTable.get(meth);
                    if (impl != null)
                        options.add(impl);
                }
            if (options.size() > 1)
                throw new RuntimeException(String.format(
                        "type %s has multiple implementations of method %s",
                        desc, meth));
            if (options.isEmpty())
                options.add(null);
            result.put(meth, options.get(0));
        }
        return cachedTable = result;
    }

    public Type compile(GlobalRCtx ctx) {
        // TODO: add inherited fields
        int numFields = instanceFields.length;

        Map<RawMethodDesc, RawMethodDesc> vtableDescs = null;
        if (!isAbstract) {
            vtableDescs = vtable(ctx);
            for (RawMethodDesc m : vtableDescs.keySet())
                if (vtableDescs.get(m) == null)
                    throw new RuntimeException(String.format(
                            "%s does not implement method %s",
                            desc, m));
        }

        for (NativeType type : God.nativeTypes())
            if (type.desc.equals(desc)) {
                // Combine native and non-native methods
                Method[] nativeMethods = type.ownedMethods;
                Method[] allMethods = new Method[methods.length];
                L:
                for (int i = 0; i < allMethods.length; ++i) {
                    for (Method natMeth : nativeMethods)
                        if (natMeth.desc.equals(methods[i].desc)) {
                            allMethods[i] = natMeth;
                            continue L;
                        }
                    allMethods[i] = methods[i].compile(new MethodRCtx(ctx, getFullDesc(), methods[i]));
                }
                type.ownedMethods = allMethods;

                type.vtableDescs = vtableDescs;
                return type;
            }

        Method[] ownedMethods = new Method[methods.length];
        for (int i = 0; i < ownedMethods.length; ++i)
            try {
                ownedMethods[i] = methods[i].compile(new MethodRCtx(ctx, getFullDesc(), methods[i]));
            } catch (Throwable e) {
                throw new RuntimeException("Compilation error in method " + methods[i].desc, e);
            }

        return new NormalType(desc, supers,
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
