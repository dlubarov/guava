package ast;

import ctx.TypeContext;
import static util.StringUtils.implode;

public class FieldDef extends MemberDef {
    private final String[] quals;
    public final TypedVar self;

    public FieldDef(String[] quals, TypedVar self) {
        this.quals = quals;
        this.self = self;
    }

    private boolean hasQualifier(String qual) {
        for (String q : quals)
            if (q.equals(qual))
                return true;
        return false;
    }

    public boolean isStatic() {
        return hasQualifier("static");
    }

    public boolean isReadOnly() {
        return hasQualifier("readonly");
    }

    public rst.FieldDef refine(TypeContext ctx) {
        return new rst.FieldDef(ctx.resolveFull(self.type), self.name,
                isStatic(), isReadOnly());
    }

    public String toString() {
        return String.format("%s %s;", implode(" ", quals), self);
    }
}
