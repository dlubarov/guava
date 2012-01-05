package parse;

import a.SourceFile;

public class SourceFileParser extends Parser<SourceFile> {
    public static final SourceFileParser singleton = new SourceFileParser();
    private SourceFileParser() {}

    @Override
    public Success<SourceFile> parse(String s, int p) {
        throw new RuntimeException("FIXME impl"); // FIXME
    }
}
