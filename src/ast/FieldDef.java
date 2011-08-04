package ast;

import ast.exp.Expression;
import ctx.TypeContext;

public class FieldDef extends MemberDef {
    private final String[] quals;
    public final TypedVar self;
    public final Expression initVal;

    public FieldDef(String[] quals, TypedVar self, Expression initVal) {
        this.quals = quals;
        this.self = self;
        this.initVal = initVal;
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
        // FIXME: initial field value
        return new rst.FieldDef(ctx.resolveFull(self.type), self.name, null,
                isStatic(), isReadOnly());
    }

    public String toString() {
        // TODO: print qualifiers
        if (initVal == null)
            return self.toString() + ';';
        return String.format("%s = %s;", self, initVal);
    }
}
