package c;

import java.util.*;

import common.NiftyException;

import c.ty.Type;

public class CodeContext {
    public final Project project;
    public final TypeDef type;
    public final MethodDef method;
    private final List<LocalInfo> locals;

    public CodeContext(Project project, TypeDef type, MethodDef method) {
        this.project = project;
        this.type = type;
        this.method = method;

        locals = new ArrayList<LocalInfo>();
        if (!method.isStatic)
            locals.add(new LocalInfo(type.thisType(), "this"));
        for (int i = 0; i < method.paramNames.length; ++i)
            locals.add(new LocalInfo(method.paramTypes[i], method.paramNames[i]));
    }

    public CodeContext(CodeContext src) {
        this.project = src.project;
        this.type = src.type;
        this.method = src.method;
        this.locals = new ArrayList<LocalInfo>(src.locals);
    }

    public Type getLocalType(String name) {
        for (LocalInfo loc : locals)
            if (loc.name.equals(name))
                return loc.type;
        throw new NoSuchElementException(String.format("no local named %s", name));
    }

    public int getLocalIndex(String name) {
        for (int i = 0; i < locals.size(); ++i)
            if (locals.get(i).name.equals(name))
                return i;
        throw new NoSuchElementException(String.format("no local named %s", name));
    }

    public int getNumLocals() {
        return locals.size();
    }

    public CodeContext addLocal(Type type, String name) {
        for (LocalInfo loc : locals)
            if (loc.name.equals(name))
                throw new NiftyException("Duplicate local: '%s'.", name);
        CodeContext newCtx = new CodeContext(this);
        newCtx.locals.add(new LocalInfo(type, name));
        return newCtx;
    }

    public static class LocalInfo {
        public final Type type;
        public final String name;

        public LocalInfo(Type type, String name) {
            this.type = type;
            this.name = name;
        }
    }
}
