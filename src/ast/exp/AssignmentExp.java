package ast.exp;

import ast.Type;
import ctx.CodeContext;
import rst.exp.*;

public class AssignmentExp extends Expression {
    private final Expression left, right;

    public AssignmentExp(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public rst.exp.Expression refine(CodeContext ctx) {
        if (left instanceof VarExp)
            return new LocalSet(ctx.localIndex(((VarExp) left).id), right.refine(ctx));

        if (left instanceof MemberAccess) {
            MemberAccess fieldAcc = (MemberAccess) left;
            if (fieldAcc.target instanceof VarExp) {
                VarExp var = (VarExp) fieldAcc.target;
                if (ctx.hasRawType(var.id))
                    return new ClassFieldSet(ctx.resolveRaw(var.id), fieldAcc.memberName, right.refine(ctx));
            }
            return new InstanceFieldSet(fieldAcc.target.refine(ctx), fieldAcc.memberName, right.refine(ctx));
        }

        if (left instanceof Invocation) {
            Invocation leftInv = (Invocation) left;
            Expression[] args = new Expression[leftInv.args.length + 1];
            System.arraycopy(leftInv.args, 0, args, 0, leftInv.args.length);
            args[leftInv.args.length] = right;
            return new Invocation(new MemberAccess(leftInv.target, "set"), new Type[0], args).refine(ctx);
        }

        throw new RuntimeException("destination of assignment is not an lval: " + left);
    }

    public String toString() {
        return String.format("(%s = %s)", left, right);
    }
}
