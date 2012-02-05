package b;

import static util.StringUtils.*;

import java.util.*;

import b.gen.TypeGenericParam;
import b.imp.*;

import common.*;

public class TypeDef {
    public Project project;
    public final WildcardImport[] wildcardImports;
    public final SpecificImport[] specificImports;

    public final TypeVisibility visibility;
    public final boolean isAbstract, isSealed;

    public final RawType desc;
    public final TypeGenericParam[] genericParams;
    public final Type[] parents;

    public final FieldDef[] staticFieldDefs, instanceFieldDefs;
    public final MethodDef[] staticMethodDefs, instanceMethodDefs;

    public TypeDef(WildcardImport[] wildcardImports, SpecificImport[] specificImports,
            TypeVisibility visibility, boolean isAbstract, boolean isSealed,
            RawType desc, TypeGenericParam[] genericParams, Type[] parents,
            FieldDef[] staticFieldDefs, FieldDef[] instanceFieldDefs,
            MethodDef[] staticMethodDefs, MethodDef[] instanceMethodDefs) {
        this.wildcardImports = wildcardImports;
        this.specificImports = specificImports;

        this.visibility = visibility;
        this.isAbstract = isAbstract;
        this.isSealed = isSealed;
        this.desc = desc;
        this.genericParams = genericParams;
        this.parents = parents;

        this.staticFieldDefs = staticFieldDefs;
        this.instanceFieldDefs = instanceFieldDefs;
        this.staticMethodDefs = staticMethodDefs;
        this.instanceMethodDefs = instanceMethodDefs;
    }

    public int genericParameterIndex(String genericParam) {
        for (int i = 0; i < genericParams.length; ++i)
            if (genericParams[i].name.equals(genericParam))
                return i;
        return -1;
    }

    public RawType qualifyType(String type) {
        List<RawType> options = new ArrayList<RawType>();
        for (SpecificImport imp : specificImports)
            if (imp.type.equals(type))
                options.add(new RawType(imp.module, type));
        if (options.isEmpty())
            for (WildcardImport imp : wildcardImports) {
                RawType qualifiedType = new RawType(imp.module, type);
                if (project.hasType(qualifiedType))
                    options.add(qualifiedType);
            }
        if (options.isEmpty())
            throw new NoSuchElementException("type not found (missing import?): " + type);
        if (options.size() > 1)
            throw new RuntimeException("conflicting imports for type: " + type);
        return options.get(0);
    }

    public boolean typeExists(String type) {
        try {
            qualifyType(type);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public c.TypeDef refine() {
        // Generic parameters
        c.gen.TypeGenericInfo[] refinedGenerics = new c.gen.TypeGenericInfo[genericParams.length];
        for (int i = 0; i < refinedGenerics.length; ++i)
            refinedGenerics[i] = genericParams[i].refine(this, i);

        // Parent types
        c.ty.ParameterizedType[] refinedParents = new c.ty.ParameterizedType[parents.length];
        for (int i = 0; i < refinedParents.length; ++i)
            refinedParents[i] = (c.ty.ParameterizedType) parents[i].refine(this, null);

        // Fields
        c.FieldDef[] refinedStaticFieldDefs = new c.FieldDef[staticFieldDefs.length],
                     refinedInstanceFieldDefs = new c.FieldDef[instanceFieldDefs.length];
        for (int i = 0; i < refinedStaticFieldDefs.length; ++i)
            refinedStaticFieldDefs[i] = staticFieldDefs[i].refine(this);
        for (int i = 0; i < refinedInstanceFieldDefs.length; ++i)
            refinedInstanceFieldDefs[i] = instanceFieldDefs[i].refine(this);

        // Methods
        c.MethodDef[] refinedStaticMethodDefs = new c.MethodDef[staticMethodDefs.length],
                      refinedInstanceMethodDefs = new c.MethodDef[instanceMethodDefs.length];
        for (int i = 0; i < refinedStaticMethodDefs.length; ++i)
            refinedStaticMethodDefs[i] = null;
        for (int i = 0; i < refinedInstanceMethodDefs.length; ++i)
            refinedInstanceMethodDefs[i] = null;

        return new c.TypeDef(
                visibility, isAbstract, isSealed,
                desc, refinedGenerics, refinedParents,
                refinedStaticFieldDefs, refinedInstanceFieldDefs,
                refinedStaticMethodDefs, refinedInstanceMethodDefs);
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
                genericParams.length == 0
                        ? ""
                        : Arrays.toString(genericParams),
                Arrays.toString(parents),
                body);
    }
}