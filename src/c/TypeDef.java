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
    public final MethodDef[] staticMethodDefs, instanceMethodDefs;

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

    public FieldDef getInstanceField(String name) {
        Set<FieldDef> options = new HashSet<FieldDef>();
        for (FieldDef field : instanceFieldDefs)
            if (field.name.equals(name))
                options.add(field);

        for (ParameterizedType parent : parents)
            try {
                options.add(owner.resolve(parent.rawType).getInstanceField(name));
            } catch (NoSuchElementException e) {}

        if (options.isEmpty())
            throw new NoSuchElementException(desc + " has no instance field named " + name);
        if (options.size() > 1)
            throw new RuntimeException(desc + " inherits multiple fields named " + name);
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
