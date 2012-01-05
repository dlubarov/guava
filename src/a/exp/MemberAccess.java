package a.exp;

public class MemberAccess extends Expression {
    public final Expression target;
    public final String memberName;

    public MemberAccess(Expression target, String memberName) {
        this.target = target;
        this.memberName = memberName;
    }

    @Override
    public b.exp.MemberAccess refine() {
        return new b.exp.MemberAccess(target.refine(), memberName);
    }

    @Override
    public String toString() {
        return target + "." + memberName;
    }
}
