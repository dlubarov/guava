package ast.stm;

import ast.TypedVar;
import ast.exp.Expression;
import ctx.CodeContext;

public class LocalDefStm extends Statement {
    public final TypedVar self;
    public final Expression initVal;

    public LocalDefStm(TypedVar self, Expression initVal) {
        this.self = self;
        this.initVal = initVal;
    }

    public RefineResult refine(CodeContext ctx) {
        CodeContext newCtx = ctx.addLocal(self.name);
        return new RefineResult(
                new rst.stm.LocalDefStm(
                        ctx.resolveFull(self.type),
                        newCtx.localIndex(self.name),
                        initVal == null ? null : initVal.refine(ctx)),
                newCtx);
    }

    public String toString() {
        if (initVal == null)
            return self.toString() + ';';
        return String.format("%s = %s;", self, initVal);
    }
}
