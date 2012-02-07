package parse;

import java.util.*;

import parse.misc.*;

import common.NiftyException;

import a.*;

public class SourceFileParser extends Parser<SourceFile> {
    public static final Parser<SourceFile> singleton = new SourceFileParser();
    private SourceFileParser() {}

    @Override
    public Success<SourceFile> parse(String s, int p) {
        p = optWS(s, p);

        // Parse the module keyword.
        Success<String> resModule = IdentifierParser.singleton.parse(s, p);
        if (resModule == null || !resModule.value.equals("module"))
            throw new NiftyException("Missing module declaration at beginning of file.");
        p = resModule.rem;
        p = optWS(s, p);

        // Parse the module name.
        resModule = IdentifierParser.singleton.parse(s, p);
        if (resModule == null)
            throw new NiftyException("Missing module name.");
        p = resModule.rem;
        p = optWS(s, p);

        // Parse the ';'.
        if (s.charAt(p++) != ';')
            throw new NiftyException("Expecting ';' after module name.");
        p = optWS(s, p);

        // Parse the imports.
        List<Import> imports = new ArrayList<Import>();
        while (p < s.length()) {
            Success<Import> resImport = ImportParser.singleton.parse(s, p);
            if (resImport == null)
                break;
            imports.add(resImport.value);
            p = resImport.rem;
            p = optWS(s, p);
        }

        // Parse the type definitions.
        List<TypeDef> typeDefs = new ArrayList<TypeDef>();
        while (p < s.length()) {
            Success<TypeDef> resTypeDef = TypeDefParser.singleton.parse(s, p);
            if (resTypeDef == null)
                throw new NiftyException("Expecting type definition.");
            typeDefs.add(resTypeDef.value);
            p = resTypeDef.rem;
            p = optWS(s, p);
        }

        // Format and return the results.
        Import[] importArr = imports.toArray(new Import[imports.size()]);
        TypeDef[] typeDefArr = typeDefs.toArray(new TypeDef[typeDefs.size()]);
        SourceFile result = new SourceFile(resModule.value, importArr, typeDefArr);
        return new Success<SourceFile>(result, p);
    }
}
