package c.stm;

import static util.StringUtils.indent;

import common.VariableGenerator;

import c.*;
import c.exp.*;
import c.ty.Type;
import d.Opcodes;

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
    public CompilationResult compile(CodeContext ctx) {
        String subjectName = VariableGenerator.randomId("subject");
        CodeContext ctxWithSubj = ctx.addLocal(Type.coreTop, subjectName);

        // Compile pattern tests.
        CodeTree[] compiledTests = new CodeTree[patterns.length];
        for (int i = 0; i < compiledTests.length; ++i)
            compiledTests[i] = patterns[i].testCode(ctxWithSubj, subjectName);

        // Compile each associated body.
        CodeTree[] compiledBodies = new CodeTree[bodies.length];
        for (int i = 0; i < compiledBodies.length; ++i) {
            CompilationResult prefix = patterns[i].prefix(ctxWithSubj, subjectName);
            CodeTree body = bodies[i].compile(prefix.newCtx).code;
            compiledBodies[i] = new CodeTree(prefix.code, body);
        }

        // Compute the number of opcodes ahead after each part of the switch.
        int[] codeAhead = new int[patterns.length];
        for (int i = codeAhead.length - 2; i >= 0; --i)
            codeAhead[i] = codeAhead[i + 1] + 5 + compiledTests[i + 1].getSize() + compiledBodies[i + 1].getSize();

        // Combine each test+body.
        CodeTree[] compiledParts = new CodeTree[patterns.length];
        for (int i = 0; i < compiledParts.length; ++i) {
            CodeTree test = compiledTests[i], body = compiledBodies[i];
            compiledParts[i] = new CodeTree(
                    // If the test fails...
                    test, Opcodes.BOOL_NEG,
                    // Jump to the next part.
                    Opcodes.JUMP_COND, body.getSize() + 2,
                    // Else, execute the body.
                    body,
                    // And jump to the end.
                    Opcodes.JUMP, codeAhead[i]
            );
        }

        CodeTree code = new CodeTree(
                subject.compile(ctx),
                Opcodes.PUT_LOCAL, ctxWithSubj.getLocalIndex(subjectName),
                new CodeTree((Object[]) compiledParts)
        );
        return new CompilationResult(code, ctx);
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
        abstract CodeTree testCode(CodeContext ctx, String subjectName);
        abstract CompilationResult prefix(CodeContext ctx, String subjectName);
    }

    public static class ValuePattern extends Pattern {
        public final Expression value;

        public ValuePattern(Expression value) {
            this.value = value;
        }

        @Override
        CodeTree testCode(CodeContext ctx, String subjectName) {
            return new InstanceMethodInvocation(
                    new LocalGet(subjectName), "==",
                    Type.NONE, new Expression[] {value}
            ).compile(ctx);
        }

        @Override
        CompilationResult prefix(CodeContext ctx, String subjectName) {
            return new CompilationResult(new CodeTree(), ctx);
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
        CodeTree testCode(CodeContext ctx, String subjectName) {
            return new InstanceMethodInvocation(
                    new LocalGet(subjectName), "instanceOf",
                    new Type[] {type}, new Expression[0]
            ).compile(ctx);
        }

        @Override
        CompilationResult prefix(CodeContext ctx, String subjectName) {
            ctx = ctx.addLocal(type, localName);
            CodeTree assignment = new CodeTree(
                    Opcodes.GET_LOCAL, ctx.getLocalIndex(subjectName),
                    Opcodes.PUT_LOCAL, ctx.getLocalIndex(localName));
            return new CompilationResult(assignment, ctx);
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
        CodeTree testCode(CodeContext ctx, String subjectName) {
            return new CodeTree(Opcodes.CONST_TRUE);
        }

        @Override
        CompilationResult prefix(CodeContext ctx, String subjectName) {
            return new CompilationResult(new CodeTree(), ctx);
        }

        @Override
        public String toString() {
            return "other";
        }
    }
}
