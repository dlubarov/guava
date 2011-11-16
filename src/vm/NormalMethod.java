package vm;

import common.*;
import vm.nat.*;
import vm.ty.*;

import static vm.Opcodes.*;
import static java.lang.System.out;

public class NormalMethod extends Method {
//    private final int numGenericParams;
    private final String[] stringTable;
    private final int numLocals;
    private final int[] code;

    public NormalMethod(RawMethodDesc desc,
                        RawTypeDesc[] typeDescTable, RawMethodDesc[] methodDescTable,
                        FullType[] fullTypeTable, String[] stringTable,
                        int numLocals, int[] code) {
        super(desc, typeDescTable, methodDescTable, fullTypeTable);
        this.stringTable = stringTable;
        this.numLocals = numLocals;
        this.code = code;
    }

    public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
        assert code != null : "invoke on abstract method";

        try {
        int sp = bp + numLocals;
        if (comp.Main.PRINT_TRACE)
            System.out.println("INVOKING " + desc);
        int ip = 0, i, j;
        ZObject a, b;
        ConcreteType[] ctypes;

        for (i = 0; i < (desc.isStatic? 0 : 1) + desc.paramTypes.length; ++i)
            assert stack[bp + i + 1] != null : "argument " + i + " is null in " + desc;

        for (;;) {
            int op;
            try {
                op = code[ip++];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException("fell off bottom of code: ip=" + (ip-1) + ", code len=" + code.length);
            }
            Type ty;
            Method meth;
            if (comp.Main.PRINT_TRACE) { // if debugging
                out.printf("    (bp=%d, sp=%d) [", bp, sp);
                for (i = 0; i <= sp; ++i)
                    out.printf(" %s ", stack[i] == null? null : stack[i].type.rawType.desc);
                out.print("]\n");
                out.printf("    %s", Opcodes.repr(op));
                switch (op) {
                    case GET_LOCAL:
                        out.printf(" %d", code[ip]);
                        break;
                    case PUT_LOCAL:
                        out.printf(" %d", code[ip]);
                        break;
                    case GET_FIELD:
                        out.printf(" %d", code[ip]);
                        break;
                    case PUT_FIELD:
                        out.printf(" %d", code[ip]);
                        break;
                    case INVOKE_STATIC:
                    case INVOKE_VIRTUAL:
                        Method m = methodTable[code[ip]];
                        out.printf(" %s.%s", m.desc.owner, m.desc.name);
                        break;
                    case CONST_INT:
                        out.printf(" %d", code[ip]);
                        break;
                }
                out.println();
            }

            switch (op) {
                case POP:
                    --sp;
                    break;

                case DUP:
                    a = stack[sp];
                    stack[++sp] = a;
                    break;

                case CONST_INT:
                    stack[++sp] = new NatInt(code[ip++]);
                    break;

                case CONST_CHAR:
                    stack[++sp] = new NatChar((char) code[ip++]);
                    break;

                case CONST_TRUE:
                    stack[++sp] = NatBool.TRUE;
                    break;

                case CONST_FALSE:
                    stack[++sp] = NatBool.FALSE;
                    break;

                case CONST_STRING:
                    String s = stringTable[code[ip++]];
                    stack[++sp] = God.makeString(s);
                    break;

                case GET_LOCAL:
                    i = code[ip++];
                    stack[++sp] = stack[bp + i + 1];
                    break;

                case PUT_LOCAL:
                    i = code[ip++];
                    stack[bp + i + 1] = stack[sp--];
                    break;

                case GET_FIELD:
                    i = code[ip++];
                    a = stack[sp--]; // target
                    stack[++sp] = ((NormalObject) a).fields[i];
                    break;

                case PUT_FIELD:
                    i = code[ip++];
                    b = stack[sp--]; // new field value
                    a = stack[sp--]; // target
                    ((NormalObject) a).fields[i] = b;
                    break;

                case GET_STATIC_FIELD:
                    i = code[ip++]; // index into type table
                    j = code[ip++]; // static field index
                    ty = typeTable[i];
                    stack[++sp] = ty.staticFields[j];
                    break;

                case PUT_STATIC_FIELD:
                    a = stack[sp--]; // new value
                    i = code[ip++]; // index into type table
                    j = code[ip++]; // static field index
                    ty = typeTable[i];
                    ty.staticFields[j] = a;
                    break;

                case INVOKE_STATIC:
                    i = code[ip++]; // index into method table
                    meth = methodTable[i];

                    i = code[ip++]; // num generic args
                    ctypes = new ConcreteType[i];
                    if (desc.isStatic)
                        a = null;
                    else
                        a = stack[bp + 1];
                    for (j = 0; j < i; ++j)
                        ctypes[j] = fullTypeTable[code[ip++]].toConcrete(a, this, genericArgs);

                    j = meth.desc.paramTypes.length; // # args
                    if (!meth.desc.isStatic)
                        j += 1;
                    meth.invoke(stack, sp - j, ctypes);
                    sp -= j - 1;
                    break;

                case INVOKE_VIRTUAL:
                    i = code[ip++]; // index into method table
                    meth = methodTable[i];
                    assert !meth.desc.isStatic;

                    i = code[ip++]; // num generic args
                    ctypes = new ConcreteType[i];
                    if (desc.isStatic)
                        a = null;
                    else
                        a = stack[bp + 1];
                    for (j = 0; j < i; ++j)
                        ctypes[j] = fullTypeTable[code[ip++]].toConcrete(a, this, genericArgs);

                    j = meth.desc.paramTypes.length; // # args (TODO: optimize)
                    a = stack[sp - j]; // target
                    ty = a.type.rawType;
                    assert ty.vtable != null : String.format(
                            "%s has null vtable (from %s, invoking %s)",
                            ty.desc, desc, meth.desc);
                    assert ty.vtable.containsKey(meth) : "method " + meth.desc
                            + " not found in vtable of " + ty.desc;
                    meth = ty.vtable.get(meth);
                    meth.invoke(stack, sp - j - 1, ctypes); // FIXME: off by 1?
                    sp -= j;
                    break;

                case NEW:
                    i = code[ip++];
                    ExternalType fty = (ExternalType) fullTypeTable[i];
                    ty = typeTable[fty.tableIndex];
                    ctypes = new ConcreteType[fty.genericArgs.length];

                    if (desc.isStatic)
                        a = null;
                    else
                        a = stack[bp + 1];

                    for (j = 0; j < ctypes.length; ++j)
                        ctypes[j] = fty.genericArgs[j].toConcrete(a, this, genericArgs);
                    stack[++sp] = ty.rawInstance(ctypes);
                    break;

                case JUMP:
                    i = code[ip++];
                    ip += i;
                    break;

                case JUMP_COND:
                    i = code[ip++];
                    if (((NatBool) stack[sp--]).value)
                        ip += i;
                    break;

                case BOOL_NEG:
                    stack[sp] = new NatBool(!((NatBool) stack[sp]).value);
                    break;

                case RETURN:
                    stack[bp + 1] = stack[sp];
                    if (comp.Main.PRINT_TRACE)
                        System.out.println("RETURNING " + stack[sp] + " from " + desc);
                    return;
            }
        }
        } catch (Exception e) {
            throw new RuntimeException("exception in " + desc, e);
        }
    }
}
