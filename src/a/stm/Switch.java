package a.stm;

import static util.StringUtils.indent;
import a.Type;
import a.exp.Expression;

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
    public b.stm.Statement refine() {
        b.stm.Switch.Pattern[] refinedPatterns = new b.stm.Switch.Pattern[patterns.length];
        for (int i = 0; i < refinedPatterns.length; ++i)
            refinedPatterns[i] = patterns[i].refine();
        return new b.stm.Switch(subject.refine(),
                refinedPatterns,
                Statement.refineAll(bodies));
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
        abstract b.stm.Switch.Pattern refine();
    }

    public static class ValuePattern extends Pattern {
        public final Expression value;

        public ValuePattern(Expression value) {
            this.value = value;
        }

        @Override
        b.stm.Switch.Pattern refine() {
            return new b.stm.Switch.ValuePattern(value.refine());
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
        b.stm.Switch.Pattern refine() {
            return new b.stm.Switch.TypePattern(type.refine(), localName);
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
        b.stm.Switch.Pattern refine() {
            return b.stm.Switch.OtherPattern.singleton;
        }

        @Override
        public String toString() {
            return "other";
        }
    }
}
