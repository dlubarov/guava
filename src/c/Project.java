package c;

import java.util.*;

import util.StringUtils;

import common.RawType;

public class Project {
    public final Map<RawType, TypeDef> typeDefs;

    public Project(TypeDef[] typeDefs) {
        this.typeDefs = new HashMap<RawType, TypeDef>();
        for (TypeDef typeDef : typeDefs) {
            typeDef.owner = this;
            this.typeDefs.put(typeDef.desc, typeDef);
        }
    }

    public boolean hasType(RawType desc) {
        return typeDefs.containsKey(desc);
    }

    public TypeDef resolve(RawType desc) {
        if (!hasType(desc))
            throw new NoSuchElementException("could not resolve type (missing import?): " + desc);
        return typeDefs.get(desc);
    }

    @Override
    public String toString() {
        return StringUtils.implode("\n\n", typeDefs.values());
    }
}
