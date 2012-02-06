package c;

import static util.StringUtils.implode;

import java.util.*;

import c.gen.*;
import c.ty.*;
import common.*;

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

        // Search my own fields
        for (FieldDef field : instanceFieldDefs)
            if (field.name.equals(name))
                options.add(field);

        // Search my parents' fields
        for (ParameterizedType parent : parents)
            try {
                options.add(owner.resolve(parent.rawType).getInstanceField(name));
            } catch (NoSuchElementException e) {}

        // There should be exactly one matching field
        if (options.isEmpty())
            throw new NoSuchElementException(desc + " has no instance field named " + name);
        if (options.size() > 1)
            throw new NiftyException("%s has multiple fields named %s", desc, name);
        return options.iterator().next();
    }

    public MethodDef getInstanceMethod(String name, Type[] genericArgs, Type[] argTypes, CodeContext ctx) {
        Set<MethodDef> options = new HashSet<MethodDef>();

        // Search my own methods
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

        // Search my parents' methods
        if (options.isEmpty())
            for (ParameterizedType parent : parents)
                try {
                    TypeDef parentDef = owner.resolve(parent.rawType);
                    MethodDef meth = parentDef.getInstanceMethod(name, genericArgs, argTypes, ctx);
                    options.add(meth);
                } catch (NoSuchElementException e) {}

        // There should be exactly one matching method
        if (options.isEmpty())
            throw new NoSuchElementException(desc + " has no instance method named " + name);
        if (options.size() > 1)
            throw new RuntimeException(desc + " has multiple instance methods matching " + name);
        return options.iterator().next();
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
        List<String> sections = new LinkedList<String>();
        if (staticFieldDefs.length > 0)
            sections.add(implode("\n", staticFieldDefs));
        if (instanceFieldDefs.length > 0)
            sections.add(implode("\n", instanceFieldDefs));
        if (staticMethodDefs.length > 0)
            sections.add(implode("\n", staticMethodDefs));
        if (instanceMethodDefs.length > 0)
            sections.add(implode("\n", instanceMethodDefs));

        String body = implode("\n\n", sections);
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
