package d;

import static d.Opcodes.*;

import java.util.Map;

import common.*;
import d.nat.NativeInt;
import d.ty.ConcreteType;
import d.ty.desc.TypeDesc;

public class BytecodeMethodDef extends ConcreteMethodDef {
    private final int numLocals;
    private final int[] code;

    public BytecodeMethodDef(RawMethod desc,
            RawType[] typeDescTable, TypeDesc[] fullTypeDescTable, RawMethod[] methodDescTable,
            Map<RawMethod, RawMethod> vDescTable,
            int numLocals, int[] bytecode) {
        super(desc, typeDescTable, fullTypeDescTable, methodDescTable, vDescTable);
        this.numLocals = numLocals;
        this.code = bytecode;
    }

    @Override
    public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
        int sp = bp + numLocals, ip = 0, op, i, j;
        BaseObject a, b;

        for (;;) {
            try {
                op = code[ip++];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NiftyException("execution ran off the end of %s without returning", desc);
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
                    stack[++sp] = new NativeInt(code[ip++]);
                    break;

                case CONST_CHAR:
                    stack[++sp] = null; // FIXME: incomplete
                    break;

                case CONST_TRUE:
                    stack[++sp] = null; // FIXME: incomplete
                    break;

                case CONST_FALSE:
                    stack[++sp] = null; // FIXME: incomplete
                    break;

                case CONST_STRING:
                    stack[++sp] = null; // FIXME: incomplete
                    break;

                case GET_LOCAL:
                    i = code[ip++]; // local index
                    stack[++sp] = null; // FIXME: incomplete
                    break;

                case PUT_LOCAL:
                    i = code[ip++]; // local index
                    // FIXME: incomplete
                    break;

                case GET_STATIC_FIELD:
                    i = code[ip++]; // index into type table
                    j = code[ip++]; // static field index
                    // FIXME: incomplete
                    break;

                case PUT_STATIC_FIELD:
                    i = code[ip++]; // index into type table
                    j = code[ip++]; // static field index
                    // FIXME: incomplete
                    break;

                case GET_INSTANCE_FIELD:
                    i = code[ip++]; // index into field table
                    // FIXME: incomplete
                    break;

                case PUT_INSTANCE_FIELD:
                    i = code[ip++]; // index into field table
                    // FIXME: incomplete
                    break;

                case INVOKE_STATIC:
                    i = code[ip++]; // index into method table
                    // FIXME: incomplete
                    break;

                case INVOKE_VIRTUAL:
                    i = code[ip++]; // index into method table
                    // FIXME: incomplete
                    break;

                case NEW:
                    i = code[ip++]; // index into type table
                    // FIXME: incomplete
                    break;

                case JUMP:
                    i = code[ip++];
                    ip += i;
                    break;

                case JUMP_COND:
                    i = code[ip++];
                    if (true) // FIXME: read bool from stack
                        ip += i;
                    break;

                case BOOL_NEG:
                    // FIXME: incomplete
                    break;

                case RETURN:
                    // TODO: this copy shouldn't be necessary if the INVOKE_s accounted for
                    // numLocals of the invoked method when reading the return value
                    stack[bp + 1] = stack[sp];
                    return;

                default:
                    throw new NiftyException("bad opcode in %s: %d", desc, op);
            }
        }
    }
}
