package b;

import static util.StringUtils.implode;

import java.util.*;

import common.*;

public class Project {
    public static Project singleton = null;

    public final Map<RawType, TypeDef> typeDefs;

    public Project(TypeDef[] typeDefs) {
        assert singleton == null : "Multiple instantiations of Project.";
        singleton = this;

        this.typeDefs = new LinkedHashMap<RawType, TypeDef>(typeDefs.length);
        for (TypeDef typeDef : typeDefs)
            this.typeDefs.put(typeDef.desc, typeDef);
    }

    public boolean hasType(RawType type) {
        return typeDefs.containsKey(type);
    }

    public c.Project refine() {
        c.TypeDef[] refinedTypeDefs = new c.TypeDef[typeDefs.size()];
        int i = 0;
        for (TypeDef typeDef : typeDefs.values())
            try {
                refinedTypeDefs[i++] = typeDef.refine();
            } catch (RuntimeException e) {
                throw new NiftyException(e, "B -> C refinement error in type '%s'.", typeDef.desc);
            }
        return new c.Project(refinedTypeDefs);
    }

    @Override
    public String toString() {
        return implode("\n\n", typeDefs.values());
    }
}
