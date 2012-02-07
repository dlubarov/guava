package a;

import util.ArrayUtils;
import common.*;

import a.Type;

import static util.StringUtils.implode;

public class FieldDef extends MemberDef {
    public final String[] quals;
    public final Type type;
    public final String[] names;

    public FieldDef(String[] quals, Type type, String[] names) {
        this.quals = quals;
        this.type = type;
        this.names = names;
    }

    public boolean hasQual(String qual) {
        for (String q : quals)
            if (q.equals(qual))
                return true;
        return false;
    }

    private FieldVisibility visibility() {
        FieldVisibility result = null;
        if (hasQual("public"))
            result = FieldVisibility.PUBLIC;
        if (hasQual("private")) {
            if (result != null)
                throw new RuntimeException("conflicting visibility qualifiers");
            result = FieldVisibility.PRIVATE;
        }
        if (result == null)
            return FieldVisibility.MODULE;
        return result;
    }

    public b.FieldDef[] refine() {
        if (ArrayUtils.hasDuplicates(quals))
            throw new NiftyException("\"%s\" has duplicate modifier", this);

        b.FieldDef[] result = new b.FieldDef[names.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = new b.FieldDef(
                    visibility(),
                    hasQual("static"),
                    hasQual("readonly"),
                    type.refine(),
                    names[i]);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String q : quals)
            sb.append(q).append(' ');
        sb.append(type).append(' ').append(implode(", ", names));
        return sb.append(';').toString();
    }
}
