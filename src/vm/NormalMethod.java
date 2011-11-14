package vm;

import common.*;
import vm.nat.*;
import vm.ty.*;

import static vm.Opcodes.*;

public class NormalMethod extends Method {
//    private final int numGenericParams;
    private final int numLocals;
    private final int[] code;

    public NormalMethod(RawMethodDesc desc,
                        RawTypeDesc[] typeDescTable, RawMethodDesc[] methodDescTable, FullType[] fullTypeTable,
                        int numLocals, int[] code) {
        super(desc, typeDescTable, methodDescTable, fullTypeTable);
        this.numLocals = numLocals;
        this.code = code;
    }

    public void invoke(ZObject[] stack, int bp, ConcreteType[] genericArgs) {
        assert code != null : "invoke on abstract method";

        int sp = bp + numLocals;
        int ip = 0, i, j;
        ConcreteType[] ctypes;

        for (;;) {
            int op = code[ip++];
            ZObject a, b;
            Type ty;
            Method meth;

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
                    a = stack[sp--]; // target
                    b = stack[sp--]; // new field value
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
                    meth = ty.vtable.get(meth);
                    assert meth != null : "method not found in vtable of " + ty;
                    meth.invoke(stack, sp - j - 1, ctypes);
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
                    stack[++ip] = ty.rawInstance(ctypes);
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
                    return;
            }
        }
    }
}
