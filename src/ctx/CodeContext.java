package ctx;

import ast.TypedVar;
import common.FullTypeDesc;
import common.RawTypeDesc;

import java.util.*;

public class CodeContext extends Resolver {
    private final MethodContext methodCtx;
    private final List<String> locals;

    public CodeContext(MethodContext methodCtx) {
        this.methodCtx = methodCtx;
        locals = new ArrayList<String>();
        if (!methodCtx.method.isStatic())
            locals.add("this");
        for (TypedVar param : methodCtx.method.params) {
            if (locals.contains(param.name))
                throw new RuntimeException(String.format("multiple locals named \"%s\"", param.name));
            locals.add(param.name);
        }
    }

    public CodeContext(CodeContext src) {
        methodCtx = src.methodCtx;
        locals = new ArrayList<String>(src.locals);
    }

    public CodeContext addLocal(String localName) {
        if (locals.contains(localName))
            throw new RuntimeException(String.format("multiple locals named \"%s\"", localName));
        CodeContext copy = new CodeContext(this);
        copy.locals.add(localName);
        return copy;
    }

    public RawTypeDesc resolveRaw(String typeName) {
        return methodCtx.resolveRaw(typeName);
    }

    @Override
    public FullTypeDesc resolveFull(String typeName) {
        return methodCtx.resolveFull(typeName);
    }

    public int localIndex(String localName) {
        int idx = locals.indexOf(localName);
        if (idx == -1)
            throw new NoSuchElementException(String.format("no local named \"%s\"", localName));
        return idx;
    }
}
