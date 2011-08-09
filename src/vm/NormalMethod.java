package vm;

import common.RawMethodDesc;
import common.RawTypeDesc;
import vm.nat.NatBool;
import vm.nat.NatInt;

import static vm.Opcodes.*;

public class NormalMethod extends Method {
//    private final int numGenericParams;
    private final int numLocals;
    private final int[] code;

    public NormalMethod(RawMethodDesc desc,
                        RawTypeDesc[] typeDescTable, RawMethodDesc[] methodDescTable,
                        int numLocals, int[] code) {
        super(desc, typeDescTable, methodDescTable);
        this.numLocals = numLocals;
        this.code = code;
    }
    
    public void invoke(ZObject[] stack, int bp) {
        int sp = bp + numLocals;
        int ip = 0, i, j;
        
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
                    j = meth.desc.paramTypes.length; // # args
                    if (!meth.desc.isStatic)
                        j += 1;
                    meth.invoke(stack, sp - j);
                    break;

                case INVOKE_VIRTUAL:
                    i = code[ip++]; // index into method table
                    meth = methodTable[i];
                    assert !meth.desc.isStatic;
                    j = meth.desc.paramTypes.length; // # args
                    ZObject target = stack[sp - j];
                    ty = target.type;
                    meth = ty.vtable.get(meth);
                    assert meth != null : "method not found in vtable of " + ty;
                    meth.invoke(stack, sp - j - 1);
                    break;

                case NEW:
                    i = code[ip++];
                    ty = typeTable[i];
                    stack[++ip] = ty.rawInstance();
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
