package ctx;

import ast.*;
import common.RawTypeDesc;

import java.util.*;

public class FileContext extends Resolver {
    private final GlobalContext globalCtx;
    public final String localModule;
    private final Set<String> wildcardImports;
    private final Map<String, Set<String>> specificImports; // by module

    public FileContext(GlobalContext globalCtx, SourceFile file) {
        this.globalCtx = globalCtx;
        localModule = file.module;
        wildcardImports = new HashSet<String>();
        specificImports = new HashMap<String, Set<String>>();
        
        // Automatic imports
        wildcardImports.add("core");
        if (!file.module.equals("core"))
            wildcardImports.add(file.module);

        Set<String> allModules = globalCtx.getLoadedModules();
        assert allModules.contains("core");
        assert allModules.contains(file.module);

        for (Import imp : file.imports) {
            if (!allModules.contains(imp.module))
                throw new RuntimeException(String.format("no module named \"%s\"", imp.module));
            if (imp.type == null)
                wildcardImports.add(imp.module);
            else {
                Set<String> typesFromMod = specificImports.get(imp.module);
                if (typesFromMod == null)
                    specificImports.put(imp.module, typesFromMod = new HashSet<String>());
                typesFromMod.add(imp.type);
            }
        }
    }

    public RawTypeDesc resolveRaw(String typeName) {
        List<RawTypeDesc> options = new ArrayList<RawTypeDesc>();
        for (String mod : specificImports.keySet())
            if (specificImports.get(mod).contains(typeName))
                options.add(new RawTypeDesc(mod, typeName));
        if (options.isEmpty())
            for (String mod : wildcardImports)
                if (globalCtx.getTypeNamesFor(mod).contains(typeName))
                    options.add(new RawTypeDesc(mod, typeName));
        if (options.isEmpty())
            throw new NoSuchElementException(String.format("no type found with name \"%s\"", typeName));
        if (options.size() > 1)
            throw new RuntimeException(String.format("ambiguous type name \"%s\", options are %s", typeName, options));
        return options.get(0);
    }
}