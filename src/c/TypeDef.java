package c;

import static util.StringUtils.*;

import java.util.*;

import c.gen.*;
import c.ty.*;
import common.*;
import d.ConcreteMethodDef;

public class TypeDef {
    public Project owner;
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
                TypeDef parentDef = owner.resolve(parent.rawType);
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

    public MethodDef getStaticMethod(String name, Type[] genericArgs, Type[] argTypes, CodeContext ctx) {
        Set<MethodDef> options = new HashSet<MethodDef>();

        methodSearch:
        for (MethodDef method : allMethodDefs) {
            if (!name.equals(method.name))
                continue;

            // TODO: allow static invocation of instance methods
            if (!method.isStatic)
                continue;

            if (genericArgs.length != method.genericInfos.length)
                continue;

            Type[] expectedTypes = method.paramTypes;
            if (argTypes.length != expectedTypes.length)
                continue;

            for (int i = 0; i < expectedTypes.length; ++i) {
                Type expectedType = expectedTypes[i];
                Type expectedTypeWithGens = expectedType.withGenericArgs(null, genericArgs);
                // TODO: enforce generic bounds
                if (!argTypes[i].isSubtype(expectedTypeWithGens, null, ctx.method))
                    continue methodSearch;
            }
        }

        // There should be exactly one matching method, which we return.
        if (options.isEmpty())
            throw new NoSuchElementException("no matching static method found");
        if (options.size() > 1)
            throw new NiftyException("ambiguous static method call %s", name);
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
                options.add(owner.resolve(parent.rawType).getInstanceField(name));
            } catch (NoSuchElementException e) {}

        // There should be exactly one matching field.
        if (options.isEmpty())
            throw new NoSuchElementException(desc + " has no instance field named " + name);
        if (options.size() > 1)
            throw new NiftyException("%s has multiple fields named %s", desc, name);
        return options.iterator().next();
    }

    public MethodDef getInstanceMethod(String name, Type[] genericArgs, Type[] argTypes, CodeContext ctx) {
        Set<MethodDef> options = new HashSet<MethodDef>();

        // Search my own methods.
        methodSearch:
        for (MethodDef meth : instanceMethodDefs) {
            if (!name.equals(meth.name))
                continue;
            if (genericArgs.length != meth.genericInfos.length)
                continue;
            if (argTypes.length != meth.paramTypes.length)
                continue;
            for (int i = 0; i < argTypes.length; ++i)
                if (!argTypes[i].isSubtype(meth.paramTypes[i], ctx))
                    continue methodSearch;
            options.add(meth);
        }

        // Search my parents' methods, unless this is a constructor since those aren't inherited.
        if (options.isEmpty() && !name.equals("init"))
            for (ParameterizedType parent : parents)
                try {
                    TypeDef parentDef = owner.resolve(parent.rawType);
                    MethodDef meth = parentDef.getInstanceMethod(name, genericArgs, argTypes, ctx);
                    options.add(meth);
                } catch (NoSuchElementException e) {}

        // There should be exactly one matching method.
        if (options.isEmpty())
            throw new NoSuchElementException(desc + " has no instance method named " + name);
        if (options.size() > 1)
            throw new RuntimeException(desc + " has multiple instance methods matching " + name);
        return options.iterator().next();
    }

    public d.TypeDef refine() {
        // Refine my generic variances.
        // TODO: This discards generic bounds. Shouldn't the VM's instanceof code use this info?
        Variance[] genericVariances = new Variance[genericInfos.length];
        for (int i = 0; i < genericVariances.length; ++i)
            genericVariances[i] = genericInfos[i].var;

        // Get a list of all my instance field names, including inherited fields.
        String[] instanceFieldNames = new String[instanceFieldDefs.length];
        // FIXME: need to include inherited instance fields.
        for (int i = 0; i < instanceFieldNames.length; ++i)
            instanceFieldNames[i] = instanceFieldDefs[i].name;

        // Compile static methods.
        d.ConcreteMethodDef[] refinedStaticMethods = new d.ConcreteMethodDef[staticMethodDefs.length];
        for (int i = 0; i < refinedStaticMethods.length; ++i)
            refinedStaticMethods[i] = (ConcreteMethodDef) staticMethodDefs[i].refine(this);

        // Compile instance methods.
        d.MethodDef[] refinedInstanceMethods = new d.MethodDef[instanceMethodDefs.length];
        for (int i = 0; i < refinedInstanceMethods.length; ++i)
            refinedInstanceMethods[i] = instanceMethodDefs[i].refine(this);

        Map<d.RawMethod, d.RawMethod> virtualMethodDescTable = new HashMap<d.RawMethod, d.RawMethod>();
        // FIXME populate vtable

        // Figure out what my generic arguments are for each supertype.
        // For example, String's generic arguments for Sequence are {Char}.
        Map<RawType, d.ty.desc.TypeDesc[]> superGenericDescs = new HashMap<RawType, d.ty.desc.TypeDesc[]>();
        for (RawType supertype : allSupertypes()) {
            ParameterizedType thisAsSuper = thisType().asSupertype(supertype, new CodeContext(owner, this, null));
            superGenericDescs.put(supertype, thisAsSuper.refine().genericArgs);
        }

        return new d.TypeDef(
                desc,
                genericVariances,
                staticFieldDefs.length,
                instanceFieldNames,
                refinedStaticMethods,
                refinedInstanceMethods,
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
        List<String> members = new LinkedList<String>();
        for (FieldDef f : staticFieldDefs)
            members.add(indent(f.toString()));
        for (FieldDef f : instanceFieldDefs)
            members.add(indent(f.toString()));
        for (MethodDef m : staticMethodDefs)
            members.add(indent(m.toString()));
        for (MethodDef m : instanceMethodDefs)
            members.add(indent(m.toString()));

        String body = implode("\n\n", members);
        if (body.isEmpty())
            body = "{}";
        else
            body = String.format("{\n%s\n}", body);

        return String.format(
                "%stype %s%s extends %s %s",
                qualsString(),
                desc,
                genericInfos.length == 0
                        ? ""
                        : Arrays.toString(genericInfos),
                Arrays.toString(parents),
                body);
    }
}
