package c;

import java.util.*;

import util.StringUtils;

import c.gen.*;
import c.ty.*;
import common.*;
import d.*;

public class TypeDef {
    public final TypeVisibility visibility;
    public final boolean isAbstract;
    public final boolean isSealed;
    public final RawType desc;
    public final TypeGenericInfo[] genericInfos;
    public final ParameterizedType[] parents;
    public final FieldDef[] staticFieldDefs, instanceFieldDefs;
    public final MethodDef[] staticMethodDefs, instanceMethodDefs, allMethodDefs;

    public TypeDef(TypeVisibility visibility, boolean isAbstract, boolean isSealed,
            RawType desc, TypeGenericInfo[] genericInfos, ParameterizedType[] parents,
            FieldDef[] staticFieldDefs, FieldDef[] instanceFieldDefs,
            MethodDef[] staticMethodDefs, MethodDef[] instanceMethodDefs) {
        this.visibility = visibility;
        this.isAbstract = isAbstract;
        this.isSealed = isSealed;
        this.desc = desc;
        this.genericInfos = genericInfos;
        this.parents = parents;

        this.staticFieldDefs = staticFieldDefs;
        this.instanceFieldDefs = instanceFieldDefs;
        this.staticMethodDefs = staticMethodDefs;
        this.instanceMethodDefs = instanceMethodDefs;

        allMethodDefs = new MethodDef[staticMethodDefs.length + instanceMethodDefs.length];
        System.arraycopy(staticMethodDefs, 0, allMethodDefs, 0, staticMethodDefs.length);
        System.arraycopy(instanceMethodDefs, 0, allMethodDefs, staticMethodDefs.length, instanceMethodDefs.length);
    }

    public ParameterizedType thisType() {
        Type[] genericArgs = new Type[genericInfos.length];
        for (int i = 0; i < genericArgs.length; ++i)
            genericArgs[i] = new TypeGenericType(i);
        return new ParameterizedType(desc, genericArgs);
    }

    public Set<RawType> allSupertypes() {
        Set<RawType> result = new HashSet<RawType>();
        result.add(desc);
        for (ParameterizedType parent : parents)
            if (!result.contains(parent.rawType)) {
                TypeDef parentDef = Project.singleton.resolve(parent.rawType);
                result.addAll(parentDef.allSupertypes());
            }
        return result;
    }

    public ParameterizedType[] parentsWithGenerics(Type[] myGenerics) {
        ParameterizedType[] result = new ParameterizedType[parents.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = parents[i].withGenericArgs(myGenerics, null);
        return result;
    }

    public int getStaticFieldIndex(String name) {
        for (int i = 0; i < staticFieldDefs.length; ++i)
            if (staticFieldDefs[i].name.equals(name))
                return i;
        throw new NoSuchElementException(
                String.format("%s has no static field named %s", desc, name));
    }

    public FieldDef getStaticField(String name) {
        return staticFieldDefs[getStaticFieldIndex(name)];
    }

    public MethodDef getStaticMethod(String name, Type[] methodGenerics, Type[] argTypes, CodeContext ctx) {
        Set<MethodDef> options = new HashSet<MethodDef>();

        instanceMethodSearch:
        if (argTypes.length > 0) {
            // Try to interpret this as a static invocation of an instance method.
            ParameterizedType thisType;
            try {
                thisType = argTypes[0].asSupertype(desc, ctx);
            } catch (IllegalArgumentException e) {
                break instanceMethodSearch;
            }

            Type[] otherArgTypes = new Type[argTypes.length - 1];
            System.arraycopy(argTypes, 1, otherArgTypes, 0, otherArgTypes.length);
            try {
                options.add(getInstanceMethod(name, thisType.genericArgs, methodGenerics, otherArgTypes, ctx));
            } catch (NoSuchElementException e) {}
        }

        methodSearch:
        for (MethodDef method : allMethodDefs) {
            if (!name.equals(method.name))
                continue;

            // TODO: allow static invocation of instance methods
            if (!method.isStatic)
                continue;

            if (methodGenerics.length != method.genericInfos.length)
                continue;

            Type[] expectedTypes = method.paramTypes;
            if (argTypes.length != expectedTypes.length)
                continue;

            for (int i = 0; i < expectedTypes.length; ++i) {
                Type expectedType = expectedTypes[i];
                Type expectedTypeWithGens = expectedType.withGenericArgs(null, methodGenerics);
                // TODO: enforce generic bounds
                if (!argTypes[i].isSubtype(expectedTypeWithGens, null, ctx.method))
                    continue methodSearch;
            }

            options.add(method);
        }

        // There should be exactly one matching method, which we return.
        if (options.isEmpty())
            throw new NoSuchElementException(String.format(
                    "No matching static method found for %s.%s with argument types %s.",
                    desc, name, Arrays.toString(argTypes)));
        if (options.size() > 1)
            throw new NiftyException("Ambiguous static method call '%s'.", name);
        return options.iterator().next();
    }

    public FieldDef getInstanceField(String name) {
        Set<FieldDef> options = new HashSet<FieldDef>();

        // Search my own fields.
        for (FieldDef field : instanceFieldDefs)
            if (field.name.equals(name))
                options.add(field);

        // Search my parents' fields.
        for (ParameterizedType parent : parents)
            try {
                options.add(Project.singleton.resolve(parent.rawType).getInstanceField(name));
            } catch (NoSuchElementException e) {}

        // There should be exactly one matching field.
        if (options.isEmpty())
            throw new NoSuchElementException(desc + " has no instance field named " + name);
        if (options.size() > 1)
            throw new NiftyException("%s has multiple fields named %s", desc, name);
        return options.iterator().next();
    }

    public MethodDef getInstanceMethod(
            String name,
            Type[] typeGenerics, Type[] methGenerics,
            Type[] argTypes,
            CodeContext ctx) {
        Set<MethodDef> options = new HashSet<MethodDef>();

        // Search my own methods.
        methodSearch:
        for (MethodDef meth : instanceMethodDefs) {
            if (meth.isStatic)
                continue;
            if (!name.equals(meth.name))
                continue;
            if (methGenerics.length != meth.genericInfos.length)
                continue;
            if (argTypes.length != meth.paramTypes.length)
                continue;
            for (int i = 0; i < argTypes.length; ++i) {
                Type foundTy = argTypes[i];
                Type expectedTy = meth.paramTypes[i].withGenericArgs(typeGenerics, methGenerics);
                if (!foundTy.isSubtype(expectedTy, ctx))
                    continue methodSearch;
            }
            options.add(meth);
        }

        // Search my parents' methods, unless this is a constructor since those aren't inherited.
        if (options.isEmpty() && !name.equals("init"))
            for (ParameterizedType parent : parentsWithGenerics(typeGenerics))
                try {
                    TypeDef parentDef = Project.singleton.resolve(parent.rawType);
                    MethodDef meth = parentDef.getInstanceMethod(
                            name, parent.genericArgs, methGenerics, argTypes, ctx);
                    options.add(meth);
                } catch (NoSuchElementException e) {}

        // There should be exactly one matching method.
        if (options.isEmpty())
            throw new NoSuchElementException(String.format(
                    "%s has no instance method named %s accepting argument types %s",
                    desc, name, Arrays.toString(argTypes)));
        if (options.size() > 1)
            throw new RuntimeException(desc + " has multiple instance methods matching " + name);
        return options.iterator().next();
    }

    private Set<FieldDef> allInheritedFields() {
        Set<FieldDef> fieldDefs = new HashSet<FieldDef>();
        fieldDefs.addAll(Arrays.asList(instanceFieldDefs));
        for (ParameterizedType parent : parents) {
            TypeDef parentDef = Project.singleton.resolve(parent.rawType);
            fieldDefs.addAll(parentDef.allInheritedFields());
        }
        return fieldDefs;
    }

    private Set<String> allInheritedFieldNames() {
        Set<FieldDef> fieldDefs = allInheritedFields();
        Set<String> names = new HashSet<String>();
        for (FieldDef fieldDef : fieldDefs) {
            String name = fieldDef.name;
            if (names.contains(name))
                throw new NiftyException("%s inherits multiple fields named '%s'.", desc, name);
            names.add(name);
        }
        return names;
    }

    // Fetch all instance methods which this type inherits, including overridden methods.
    // These are used to generate keys for the virtual method descriptor table.
    private Set<MethodDef> allInheritedInstanceMethods() {
        Set<MethodDef> methods = new HashSet<MethodDef>();
        for (MethodDef m : instanceMethodDefs)
            methods.add(m);
        for (ParameterizedType parent : parents) {
            TypeDef parentDef = Project.singleton.resolve(parent.rawType);
            methods.addAll(parentDef.allInheritedInstanceMethods());
        }
        return methods;
    }

    private boolean hasSupertype(RawType sup) {
        try {
            thisType().asSupertype(sup, new CodeContext(this, null));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Find my implementation of an instance method.
    private MethodDef getImplementation(MethodDef method) {
        if (!hasSupertype(method.owner))
            throw new NoSuchElementException("This should be caught...");

        // Try my instance methods.
        Set<MethodDef> options = new HashSet<MethodDef>();
        for (MethodDef m : instanceMethodDefs)
            if (m.canImplement(method))
                options.add(m);

        // If nothing was found yet, try my parents.
        if (options.isEmpty())
            for (ParameterizedType parent : parents) {
                TypeDef parentDef = Project.singleton.resolve(parent.rawType);
                try {
                    options.add(parentDef.getImplementation(method));
                } catch (NoSuchElementException e) {}
            }

        if (options.isEmpty())
            throw new NoSuchElementException(String.format(
                    "Type '%s' has no implementation of '%s.%s'.",
                    desc, method.owner, method.name));

        // Attempt to narrow down to 1 with canImplement checks.
        // Example: Suppose Sequence[T] overrides Top.hashCode while Immutable inherits it,
        // and BitSet implements Sequence[Bool] and Immutable. Then BitSet will have two
        // implementations of hashCode, Top's and Sequence's. This code should eliminate
        // Top.hashCode from options.
        for (MethodDef m1 : new HashSet<MethodDef>(options))
            for (MethodDef m2 : options)
                // If m2 overrides m1, remove m1 from options.
                if (m2 != m1 && m2.canImplement(m1)) {
                    options.remove(m1);
                    break;
                }

        if (options.size() > 1) {
            String[] optionDescs = new String[options.size()];
            int i = 0;
            for (MethodDef m : options)
                optionDescs[i++] = m.owner + " " + m.name;
            throw new NiftyException(
                    "Type '%s' has multiple implementations of '%s.%s': %s.",
                    desc, method.owner, method.name, Arrays.toString(optionDescs));
        }

        return options.iterator().next();
    }

    public d.TypeDef compile() {
        // Refine my generic variances.
        // TODO: This discards generic bounds. Shouldn't the VM's instanceof code use this info?
        Variance[] genericVariances = new Variance[genericInfos.length];
        for (int i = 0; i < genericVariances.length; ++i)
            genericVariances[i] = genericInfos[i].var;

        // Get a list of all my instance field names, including inherited fields.
        Set<String> instanceFieldNames = allInheritedFieldNames();
        String[] instanceFieldNamesArr = instanceFieldNames.toArray(new String[instanceFieldNames.size()]);

        // Compile static methods.
        d.ConcreteMethodDef[] compiledStaticMethods = new d.ConcreteMethodDef[staticMethodDefs.length];
        for (int i = 0; i < compiledStaticMethods.length; ++i)
            try {
                compiledStaticMethods[i] = (ConcreteMethodDef) staticMethodDefs[i].compile(this);
            } catch (RuntimeException e) {
                throw new NiftyException(e,
                        "Refinement (c->d) error in static method '%s'.",
                        staticMethodDefs[i].name);
            }

        // Compile instance methods.
        d.MethodDef[] compiledInstanceMethods = new d.MethodDef[instanceMethodDefs.length];
        for (int i = 0; i < compiledInstanceMethods.length; ++i) {
            MethodDef m = instanceMethodDefs[i];
            try {
                compiledInstanceMethods[i] = m.compile(this);
            } catch (RuntimeException e) {
                throw new NiftyException(e,
                        "Refinement (c->d) error in instance method %s.",
                        m.refineDesc());
            }
        }

        // Populate the virtual method descriptor table.
        Map<d.RawMethod, d.RawMethod> virtualMethodDescTable;
        if (isAbstract)
            virtualMethodDescTable = null;
        else {
            virtualMethodDescTable = new HashMap<d.RawMethod, d.RawMethod>();
            for (MethodDef m : allInheritedInstanceMethods()) {
                MethodDef impl = getImplementation(m);
                virtualMethodDescTable.put(m.refineDesc(), impl.refineDesc());
            }
        }

        // Figure out what my generic arguments are for each supertype.
        // For example, String's generic arguments for Sequence are {Char}.
        Map<RawType, d.ty.desc.TypeDesc[]> superGenericDescs = new HashMap<RawType, d.ty.desc.TypeDesc[]>();
        for (RawType supertype : allSupertypes()) {
            ParameterizedType thisAsSuper = thisType().asSupertype(supertype, new CodeContext(this, null));
            superGenericDescs.put(supertype, thisAsSuper.refine().genericArgs);
        }

        // If this is a native type, we don't want to create a new d.TypeDef, because
        // a NativeTypeDef already exists. However, that NativeTypeDef is missing some
        // information which we need to fill in.
        if (God.hasType(desc)) {
            d.TypeDef existingType = God.resolveType(desc);
            d.nat.NativeTypeDef nativeType = (d.nat.NativeTypeDef) existingType;

            nativeType.genericVariances = genericVariances;
            nativeType.virtualMethodDescTable = virtualMethodDescTable;
            nativeType.superGenericDescs = superGenericDescs;

            Set<d.ConcreteMethodDef> allStaticMethods = new HashSet<d.ConcreteMethodDef>();
            Set<d.MethodDef> allInstanceMethods = new HashSet<d.MethodDef>();
            for (d.ConcreteMethodDef m : nativeType.staticMethods) {
                allStaticMethods.add(m);
                // TODO: hack to replace the non-native dummy method in God's map
                God.newMethod(m);
            }
            for (d.MethodDef m : nativeType.instanceMethods) {
                allInstanceMethods.add(m);
                // TODO: hack to replace the non-native dummy method in God's map
                God.newMethod(m);
            }
            for (d.ConcreteMethodDef m : compiledStaticMethods) {
                boolean contained = false;
                for (d.ConcreteMethodDef m2 : allStaticMethods)
                    if (m2.desc.equals(m.desc))
                        contained = true;
                if (!contained)
                    allStaticMethods.add(m);
            }
            for (d.MethodDef m : compiledInstanceMethods) {
                boolean contained = false;
                for (d.MethodDef m2 : allInstanceMethods)
                    if (m2.desc.equals(m.desc))
                        contained = true;
                if (!contained)
                    allInstanceMethods.add(m);
            }
            nativeType.staticMethods = allStaticMethods.toArray(new d.ConcreteMethodDef[0]);
            nativeType.instanceMethods = allInstanceMethods.toArray(new d.MethodDef[0]);

            return nativeType;
        }

        return new d.TypeDef(
                desc,
                genericVariances,
                staticFieldDefs.length,
                instanceFieldNamesArr,
                compiledStaticMethods,
                compiledInstanceMethods,
                virtualMethodDescTable,
                superGenericDescs);
    }

    private String qualsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(visibility);
        if (isAbstract)
            sb.append("abstract ");
        if (isSealed)
            sb.append("sealed ");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(qualsString());
        sb.append("type ").append(desc);

        if (genericInfos.length > 0)
            sb.append(Arrays.toString(genericInfos));

        if (parents.length > 0)
            sb.append(" extends ").append(StringUtils.implode(", ", parents));

        sb.append(" {");
        for (Object[] memberDefs : new Object[][] {
                staticFieldDefs, instanceFieldDefs, staticMethodDefs, instanceMethodDefs})
            for (Object memberDef : memberDefs)
                sb.append('\n').append(StringUtils.indent(memberDef)).append('\n');
        return sb.append('}').toString();
    }
}
