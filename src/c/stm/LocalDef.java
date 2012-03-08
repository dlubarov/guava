package c.stm;

import java.util.*;

import c.*;
import c.exp.*;
import c.ty.Type;
import d.Opcodes;

public class LocalDef extends Statement {
    public final Type type;
    public final String[] names;
    public final Expression[] initVals;

    public LocalDef(Type type, String[] names, Expression[] initVals) {
        this.type = type;
        this.names = names;
        this.initVals = initVals;
    }

    @Override
    public CompilationResult compile(CodeContext ctx) {
        List<CodeTree> assignments = new ArrayList<CodeTree>();
        for (int i = 0; i < names.length; ++i) {
            // TODO: this is not good, it allows "Int x = x;".
            // But adding the local after generating the assignment code is problematic,
            // because LocalAssignment needs the destination local index.
            ctx = ctx.addLocal(type, names[i]);
            if (initVals[i] != null) {
                CodeTree assignment = new CodeTree(
                                initVals[i].compile(type, ctx),
                                Opcodes.PUT_LOCAL,
                                ctx.getLocalIndex(names[i]));
                assignments.add(assignment);
            }
        }
        CodeTree allCode = new CodeTree(assignments.toArray());
        return new CompilationResult(allCode, ctx);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(' ');
        for (int i = 0; i < names.length; ++i) {
            if (i > 0)
                sb.append(", ");
            sb.append(names[i]);
            if (initVals[i] != null)
                sb.append(" = ").append(initVals[i]);
        }
        sb.append(';');
        return sb.toString();
    }
}
