package vm;

import common.RawMethodDesc;
import vm.nat.NatBool;
import vm.nat.NatInt;

import static vm.Opcodes.*;

public class NormalMethod extends Method {
//    private final int numGenericParams;
    private final int numLocals;
    private final int[] code;

    public NormalMethod(RawMethodDesc desc, int numLocals, int[] code) {
        super(desc);
        this.numLocals = numLocals;
        this.code = code;
    }
    
    public void invoke(TObject[] stack, int bp) {
        int sp = bp + numLocals;
        int ip = 0, i;
        
        for (;;) {
            int op = code[ip++];
            TObject a, b;

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

                case INVOKE_STATIC:
                    i = sp; // new bp
                    // FIXME: impl
                    break;

                case RETURN_OBJECT:
                    stack[bp + 1] = stack[sp];
                    return;
            }
        }
    }
}
