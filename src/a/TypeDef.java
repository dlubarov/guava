package a;

import static util.StringUtils.indent;

import java.util.*;

import util.ArrayUtils;

import a.gen.*;
import b.imp.*;

import common.*;

public class TypeDef {
    public final GenericConstraint[] genericConstraints;
    public final String[] quals;
    public final String name;
    public final TypeGenericParam[] genericParams;
    public final Type[] parents;
    public final MemberDef[] memberDefs;

    public TypeDef(GenericConstraint[] genericConstraints, String[] quals, String name,
            TypeGenericParam[] genericParams, Type[] parents, MemberDef[] memberDefs) {
        this.genericConstraints = genericConstraints;
        this.quals = quals;
        this.name = name;
        this.genericParams = genericParams;
        this.parents = parents;
        this.memberDefs = memberDefs;
    }

    public boolean hasQual(String qual) {
        for (String q : quals)
            if (q.equals(qual))
                return true;
        return false;
    }

    public b.TypeDef refine(SourceFile source) {
        String module = source.module;
        Import[] imports = source.imports;

        // Validity checks.
        if (ArrayUtils.hasDuplicates(quals))
            throw new NiftyException("%s.%s has duplicate modifier", module, name);
        // TODO: check for invalid qualifiers.

        // Refine imports.
        Set<WildcardImport> wildcardImports = new HashSet<WildcardImport>();
        Set<SpecificImport> specificImports = new HashSet<SpecificImport>();
        for (int i = 0; i < imports.length; ++i) {
            Import imp = imports[i];
            if (imp.type == null)
                wildcardImports.add(new WildcardImport(imp.module));
            else
                specificImports.add(new SpecificImport(imp.module, imp.type));
        }
        wildcardImports.add(new WildcardImport("core"));
        wildcardImports.add(new WildcardImport(module));
        specificImports.add(new SpecificImport(module, name));

        // Refine generic parameters.
        b.gen.TypeGenericParam[] refinedGenericParams = new b.gen.TypeGenericParam[genericParams.length];
        for (int i = 0; i < refinedGenericParams.length; ++i) {
            TypeGenericParam genericParam = genericParams[i];
            Set<b.Type> subOf = new HashSet<b.Type>(), supOf = new HashSet<b.Type>();
            for (GenericConstraint constraint : genericConstraints)
                if (constraint.genericParam.equals(genericParam.name))
                    switch (constraint.rel) {
                        case SUBTYPE:
                            subOf.add(constraint.otherType.refine());
                            break;
                        case SUPERTYPE:
                            supOf.add(constraint.otherType.refine());
                            break;
                    }
            if (subOf.isEmpty())
                subOf.add(new b.Type("Top"));
            if (supOf.isEmpty())
                supOf.add(new b.Type("Bottom"));
            refinedGenericParams[i] = new b.gen.TypeGenericParam(
                    genericParam.var,
                    genericParam.name,
                    subOf.toArray(new b.Type[subOf.size()]),
                    supOf.toArray(new b.Type[supOf.size()]));
        }

        // Refine parent types.
        b.Type[] refinedParents;
        if (parents.length == 0 && !(module.equals("core") && name.equals("Top")))
            refinedParents = new b.Type[] {new b.Type("Top")};
        else
            refinedParents = Type.refineAll(parents);

        // Refine members.
        Set<b.FieldDef> staticFieldDefs = new LinkedHashSet<b.FieldDef>(),
                        instanceFieldDefs = new LinkedHashSet<b.FieldDef>();
        Set<b.MethodDef> staticMethodDefs = new LinkedHashSet<b.MethodDef>(),
                         instanceMethodDefs = new LinkedHashSet<b.MethodDef>();
        for (MemberDef memberDef : memberDefs)
            if (memberDef instanceof FieldDef) {
                b.FieldDef[] fieldDefs = ((FieldDef) memberDef).refine();
                for (b.FieldDef fieldDef : fieldDefs)
                    if (fieldDef.isStatic)
                        staticFieldDefs.add(fieldDef);
                    else
                        instanceFieldDefs.add(fieldDef);
            } else {
                b.MethodDef methodDef = ((MethodDef) memberDef).refine();
                if (methodDef.isStatic)
                    staticMethodDefs.add(methodDef);
                else
                    instanceMethodDefs.add(methodDef);
            }

        return new b.TypeDef(
                wildcardImports.toArray(new WildcardImport[wildcardImports.size()]),
                specificImports.toArray(new SpecificImport[specificImports.size()]),
                hasQual("public") ? TypeVisibility.PUBLIC : TypeVisibility.MODULE,
                hasQual("abstract"), hasQual("sealed"),
                new RawType(module, name),
                refinedGenericParams,
                refinedParents,
                staticFieldDefs.toArray(new b.FieldDef[staticFieldDefs.size()]),
                instanceFieldDefs.toArray(new b.FieldDef[instanceFieldDefs.size()]),
                staticMethodDefs.toArray(new b.MethodDef[staticMethodDefs.size()]),
                instanceMethodDefs.toArray(new b.MethodDef[instanceMethodDefs.size()]));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String q : quals)
            sb.append(q).append(' ');
        sb.append("type ").append(name).append(" {");
        for (int i = 0; i < memberDefs.length; ++i) {
            sb.append("\n");
            sb.append(indent(memberDefs[i]));
            sb.append("\n");
        }
        return sb.append("}").toString();
    }
}
