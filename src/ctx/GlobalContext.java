package ctx;

import ast.*;
import ast.TypeDef;
import common.RawTypeDesc;

import java.util.*;

public class GlobalContext {
    // Type definitions indexed by module, then by type name
    private final Map<String, Map<String, TypeDef>> typeDefs;
    
    public GlobalContext(SourceFile[] sources) {
        typeDefs = new HashMap<String, Map<String, TypeDef>>();
        for (SourceFile src : sources) {
            Map<String, TypeDef> typesInMod = typeDefs.get(src.module);
            if (typesInMod == null)
                typeDefs.put(src.module, typesInMod = new HashMap<String, TypeDef>());
            for (TypeDef type : src.types)
                if (typesInMod.put(type.name, type) != null)
                    throw new RuntimeException(String.format("two types share the same qualified name: %s.%s",
                                    src.module, type.name));
        }
    }

    public Set<String> getLoadedModules() {
        return typeDefs.keySet();
    }
    
    // TODO: not needed?
    public Set<RawTypeDesc> getTypeDescsFor(String module) {
        Set<RawTypeDesc> result = new HashSet<RawTypeDesc>();
        for (String typeName : typeDefs.get(module).keySet())
            result.add(new RawTypeDesc(module, typeName));
        return result;
    }

    public Set<String> getTypeNamesFor(String module) {
        Map<String, TypeDef> typesByName = typeDefs.get(module);
        if (typesByName == null)
            throw new NoSuchElementException(String.format("no module named \"%s\"", module));
        return typesByName.keySet();
    }

    // TODO: is there a more natural place for the top-level refine method?
    public static rst.TypeDef[] refine(SourceFile[] sources) {
        GlobalContext ctx = new GlobalContext(sources);
        List<rst.TypeDef> result = new ArrayList<rst.TypeDef>();
        for (SourceFile src : sources)
            for (rst.TypeDef type : src.refine(new FileContext(ctx, src)))
                result.add(type);
        return result.toArray(new rst.TypeDef[result.size()]);
    }
}
