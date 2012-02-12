package a.exp;

import common.NiftyException;

import a.*;

public class Assignment extends Expression {
    public final Expression left, right;

    public Assignment(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public b.exp.Expression refine() {
        // Local assignment: x = val
        if (left instanceof Variable)
            return new b.exp.LocalAssignment(((Variable) left).name, right.refine());

        // Field assignment: exp.x = val
        if (left instanceof MemberAccess) {
            MemberAccess memAcc = (MemberAccess) left;
            return new b.exp.FieldAssignment(
                    memAcc.target.refine(),
                    memAcc.memberName,
                    right.refine());
        }

        // exp(x, y) = val is sugar for exp.set(x, y, val)
        if (left instanceof Invocation) {
            Invocation inv = (Invocation) left;
            if (inv.genericArgs.length > 0)
                throw new NiftyException("wtf is this? %s", this);
            Expression[] args = new Expression[inv.args.length + 1];
            System.arraycopy(inv.args, 0, args, 0, inv.args.length);
            args[inv.args.length] = right;
            Expression newTarget = new MemberAccess(inv.target, "set");
            return new Invocation(newTarget, Type.NONE, args).refine();
        }

        throw new NiftyException("assignment to non-lval: %s", left);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", left, right);
    }
}
