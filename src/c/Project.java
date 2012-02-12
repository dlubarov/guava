package c;

import java.util.*;

import util.StringUtils;

import common.*;

public class Project {
    public final HashMap<RawType, TypeDef> typeDefs;

    public Project(TypeDef[] typeDefs) {
        this.typeDefs = new LinkedHashMap<RawType, TypeDef>(typeDefs.length);
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

    public d.TypeDef[] compile() {
        Set<d.TypeDef> result = new LinkedHashSet<d.TypeDef>();
        for (TypeDef typeDef : typeDefs.values())
            try {
                result.add(typeDef.compile());
            } catch (RuntimeException e) {
                throw new NiftyException(e, "Refinement (c->d) error in type '%s'.", typeDef.desc);
            }
        return result.toArray(new d.TypeDef[result.size()]);
    }

    @Override
    public String toString() {
        return StringUtils.implode("\n\n", typeDefs.values());
    }
}
