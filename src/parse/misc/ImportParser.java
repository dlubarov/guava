package parse.misc;

import parse.*;
import common.NiftyException;

import a.Import;

public class ImportParser extends Parser<Import> {
    public static final Parser<Import> singleton = new ImportParser().memo();
    private ImportParser() {}

    @Override
    public Success<Import> parse(String s, int p) {
        // Parse the "import" keyword.
        Success<String> resImport = IdentifierParser.singleton.parse(s, p);
        if (resImport == null || !resImport.value.equals("import"))
            return null;
        p = resImport.rem;
        p = optWS(s, p);

        // Parse the module name.
        Success<String> resModule = IdentifierParser.singleton.parse(s, p);
        if (resModule == null)
            throw new NiftyException("missing module name after 'import'.");
        String module = resModule.value;
        p = resModule.rem;
        p = optWS(s, p);

        // Parse the '.'.
        if (s.charAt(p++) != '.')
            throw new NiftyException("Missing '.' after module name in import.");
        p = optWS(s, p);

        // Parse the type name.
        String type;
        if (s.charAt(p) == '*') {
            ++p;
            type = null;
        } else {
            Success<String> resType = IdentifierParser.singleton.parse(s, p);
            if (resType == null)
                throw new NiftyException("missing type name after 'import %s.'.", module);
            type = resType.value;
            p = resType.rem;
        }
        p = optWS(s, p);

        // Parse the ';'.
        if (s.charAt(p++) != ';')
            throw new NiftyException("Missing ';' at end of import.");

        Import result = new Import(module, type);
        return new Success<Import>(result, p);
    }
}
