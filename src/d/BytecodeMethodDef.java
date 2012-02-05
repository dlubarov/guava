package d;

import static d.Opcodes.*;

import java.util.Map;

import common.*;
import d.nat.*;
import d.ty.ConcreteType;
import d.ty.desc.TypeDesc;
import d.ty.nf.NonFinalType;

public class BytecodeMethodDef extends ConcreteMethodDef {
    public final String[] stringTable;

    private final int numLocals;
    private final int[] code;

    public BytecodeMethodDef(RawMethod desc,
            RawType[] typeDescTable,
            TypeDesc[] fullTypeDescTable,
            RawMethod[] methodDescTable,
            String[] stringTable,
            Map<RawMethod, RawMethod> vDescTable,
            int numLocals, int[] bytecode) {
        super(desc, typeDescTable, fullTypeDescTable, methodDescTable, vDescTable);
        this.stringTable = stringTable;
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
                    stack[++sp] = new NativeChar((char) code[ip++]);
                    break;

                case CONST_TRUE:
                    stack[++sp] = NativeBool.TRUE;
                    break;

                case CONST_FALSE:
                    stack[++sp] = NativeBool.FALSE;
                    break;

                case CONST_STRING:
                    stack[++sp] = VMUtils.makeString(stringTable[ip++]);
                    break;

                case GET_LOCAL:
                    i = code[ip++]; // local index
                    stack[++sp] = stack[bp + i + 1];
                    break;

                case PUT_LOCAL:
                    i = code[ip++]; // local index
                    stack[bp + i + 1] = stack[sp--];
                    break;

                case GET_STATIC_FIELD:
                    i = code[ip++]; // index into type table
                    j = code[ip++]; // static field index
                    stack[++sp] = rawTypeTable[i].staticFields[j];
                    break;

                case PUT_STATIC_FIELD:
                    a = stack[sp--];
                    i = code[ip++]; // index into type table
                    j = code[ip++]; // static field index
                    rawTypeTable[i].staticFields[j] = a;
                    break;

                case GET_INSTANCE_FIELD:
                    a = stack[sp--]; // target
                    i = code[ip++]; // index into field table
                    stack[++sp] = a.fields[i];
                    break;

                case PUT_INSTANCE_FIELD:
                    b = stack[sp--]; // new field value
                    a = stack[sp--]; // target
                    i = code[ip++]; // index into field table
                    a.fields[i] = b;
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
                    i = code[ip++]; // index into full type table
                    a = desc.isStatic ? null : stack[bp + 1]; // current object
                    stack[++sp] = fullTypeTable[i].toConcrete(a, genericArgs).rawInstance();
                    break;

                case JUMP:
                    i = code[ip++];
                    ip += i;
                    break;

                case JUMP_COND:
                    i = code[ip++];
                    if (((NativeBool) stack[sp--]).value)
                        ip += i;
                    break;

                case BOOL_NEG:
                    stack[sp] = new NativeBool(!((NativeBool) stack[sp]).value);
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
