package a;

import static util.StringUtils.implode;

import java.util.*;

public class Project {
    public static Project singleton = null;

    public final SourceFile[] sourceFiles;

    public Project(SourceFile[] sourceFiles) {
        assert singleton == null : "Multiple instantiations of Project.";
        singleton = this;

        this.sourceFiles = sourceFiles;
    }

    public b.Project refine() {
        Set<b.TypeDef> typeDefs = new LinkedHashSet<b.TypeDef>();
        for (SourceFile sourceFile : sourceFiles)
            for (b.TypeDef typeDef : sourceFile.refine())
                typeDefs.add(typeDef);
        return new b.Project(typeDefs.toArray(new b.TypeDef[typeDefs.size()]));
    }

    @Override
    public String toString() {
        return implode("\n\n", sourceFiles);
    }
}
