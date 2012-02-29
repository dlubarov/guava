package b.stm;

import static util.StringUtils.indent;
import b.*;
import b.exp.Expression;

public class Switch extends Statement {
    public final Expression subject;
    public final Pattern[] patterns;
    public final Statement[] bodies;

    public Switch(Expression subject, Pattern[] patterns, Statement[] bodies) {
        this.subject = subject;
        this.patterns = patterns;
        this.bodies = bodies;
    }

    @Override
    public c.stm.Statement refine(TypeDef typeCtx, MethodDef methodCtx) {
        c.stm.Switch.Pattern[] refinedPatterns = new c.stm.Switch.Pattern[patterns.length];
        for (int i = 0; i < refinedPatterns.length; ++i)
            refinedPatterns[i] = patterns[i].refine(typeCtx, methodCtx);
        return new c.stm.Switch(
                subject.refine(typeCtx, methodCtx),
                refinedPatterns,
                Statement.refineAll(bodies, typeCtx, methodCtx));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("switch (").append(subject).append(") {");
        for (int i = 0; i < patterns.length; ++i) {
            StringBuilder part = new StringBuilder();
            part.append(patterns[i]).append(" -> ").append(bodies[i]);
            sb.append('\n').append(indent(part));
        }
        return sb.append("\n}").toString();
    }

    public static abstract class Pattern {
        abstract c.stm.Switch.Pattern refine(TypeDef typeCtx, MethodDef methodCtx);
    }

    public static class ValuePattern extends Pattern {
        public final Expression value;

        public ValuePattern(Expression value) {
            this.value = value;
        }

        @Override
        c.stm.Switch.Pattern refine(TypeDef typeCtx, MethodDef methodCtx) {
            return new c.stm.Switch.ValuePattern(value.refine(typeCtx, methodCtx));
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    public static class TypePattern extends Pattern {
        public final Type type;
        public final String localName;

        public TypePattern(Type type, String localName) {
            this.type = type;
            this.localName = localName;
        }

        @Override
        c.stm.Switch.Pattern refine(TypeDef typeCtx, MethodDef methodCtx) {
            return new c.stm.Switch.TypePattern(type.refine(typeCtx, methodCtx), localName);
        }

        @Override
        public String toString() {
            return type + " " + localName;
        }
    }

    public static class OtherPattern extends Pattern {
        public static final OtherPattern singleton = new OtherPattern();
        private OtherPattern() {}

        @Override
        c.stm.Switch.Pattern refine(TypeDef typeCtx, MethodDef methodCtx) {
            return c.stm.Switch.OtherPattern.singleton;
        }

        @Override
        public String toString() {
            return "other";
        }
    }
}
