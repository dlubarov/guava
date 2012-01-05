package b;

import static util.StringUtils.implode;

import java.util.*;

import common.RawType;

public class Project {
    public final Map<RawType, TypeDef> typeDefs;

    public Project(TypeDef[] typeDefs) {
        this.typeDefs = new HashMap<RawType, TypeDef>();
        for (TypeDef typeDef : typeDefs) {
            typeDef.project = this;
            this.typeDefs.put(typeDef.desc, typeDef);
        }
    }

    public boolean hasType(RawType type) {
        return typeDefs.containsKey(type);
    }

    public c.Project refine() {
        c.TypeDef[] refinedTypeDefs = new c.TypeDef[typeDefs.size()];
        int i = 0;
        for (TypeDef typeDef : typeDefs.values())
            refinedTypeDefs[i++] = typeDef.refine();
        return new c.Project(refinedTypeDefs);
    }

    @Override
    public String toString() {
        return implode("\n\n", typeDefs.values());
    }
}
