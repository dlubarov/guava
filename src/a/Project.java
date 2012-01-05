package a;

import static util.StringUtils.implode;

import java.util.*;

public class Project {
    public final SourceFile[] sourceFiles;

    public Project(SourceFile[] sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public b.Project refine() {
        Set<b.TypeDef> typeDefs = new HashSet<b.TypeDef>();
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
