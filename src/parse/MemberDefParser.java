package parse;

import a.*;

public class MemberDefParser extends Parser<MemberDef> {
    public static final Parser<MemberDef> singleton = new MemberDefParser();
    private MemberDefParser() {}

    @Override
    public Success<MemberDef> parse(String s, int p) {
        Success<MemberDef> result = FieldDefParser.singleton.parse(s, p);
        if (result == null)
            result = MethodDefParser.singleton.parse(s, p);
        return result;
    }
}
