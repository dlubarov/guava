package vm;

import common.*;

import java.util.*;

import vm.ty.*;

public abstract class Type {
    public final RawTypeDesc desc;

    private final NormalFullTypeDesc[] superDescs;
    private SuperCompoundType[] superTypes;

    public final Method[] ownedMethods;

    public Map<RawMethodDesc, RawMethodDesc> vtableDescs;
    public Map<Method, Method> vtable;

    public ZObject[] staticFields;

    public Type(RawTypeDesc desc,
                NormalFullTypeDesc[] superDescs,
                Method[] ownedMethods,
                Map<RawMethodDesc, RawMethodDesc> vtableDescs,
                int numStaticFields) {
        this.desc = desc;
        this.superDescs = superDescs;
        this.ownedMethods = ownedMethods;
        this.vtableDescs = vtableDescs;
        vtable = null;
        staticFields = new ZObject[numStaticFields];
        God.newType(this);
    }

    private SuperType resolveSuperType(FullTypeDesc desc) {
        if (desc instanceof TypeGenericFullTypeDesc) {
            TypeGenericFullTypeDesc tgDesc = (TypeGenericFullTypeDesc) desc;
            return new SuperGenericType(tgDesc.index);
        } else {
            NormalFullTypeDesc ftDesc = (NormalFullTypeDesc) desc;
            SuperType[] args = new SuperType[ftDesc.genericArgs.length];
            for (int i = 0; i < args.length; ++i)
                args[i] = resolveSuperType(ftDesc.genericArgs[i]);
            return new SuperCompoundType(God.resolveType(ftDesc.raw), args);
        }
    }

    public void link() {
        superTypes = new SuperCompoundType[superDescs.length];
        for (int i = 0; i < superTypes.length; ++i)
            superTypes[i] = (SuperCompoundType) resolveSuperType(superDescs[i]);

        for (Method m : ownedMethods)
            m.link();

        if (vtableDescs != null) { // if not abstract
            vtable = new HashMap<Method, Method>();
            for (Map.Entry<RawMethodDesc, RawMethodDesc> e : vtableDescs.entrySet())
                vtable.put(God.resolveMethod(e.getKey()), God.resolveMethod(e.getValue()));
        }
    }

    public abstract ZObject rawInstance(ConcreteType[] genericArgs);

    public ConcreteType fetchGenericArgument(Type targetType, int genericIndex, ConcreteType[] myGenericArgs) {
        if (targetType == this)
            return myGenericArgs[genericIndex];
        for (SuperCompoundType sup : superTypes) {
            Type supRaw = sup.mainType;
            ConcreteType[] supGenericArgs = new ConcreteType[sup.genericArguments.length];
            for (int i = 0; i < supGenericArgs.length; ++i)
                supGenericArgs[i] = sup.genericArguments[i].toConcrete(myGenericArgs);
            ConcreteType result = supRaw.fetchGenericArgument(targetType, genericIndex, supGenericArgs);
            if (result != null)
                return result;
        }
        return null;
    }

    public String toString() {
        return desc.toString();
    }
}
