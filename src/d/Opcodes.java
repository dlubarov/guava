package d;

import java.util.Arrays;

import common.*;

import d.ty.desc.TypeDesc;

public final class Opcodes {
    public static final int
            POP = 0,
            DUP = 1,
            CONST_TRUE = 10,
            CONST_FALSE = 11,
            CONST_INT = 12,
            CONST_CHAR = 13,
            CONST_DOUBLE = 14,
            CONST_STRING = 15,
            CREATE_SEQ = 18,
            GET_LOCAL = 20,
            PUT_LOCAL = 21,
            GET_STATIC_FIELD = 22,
            PUT_STATIC_FIELD = 23,
            GET_INSTANCE_FIELD = 24,
            PUT_INSTANCE_FIELD = 25,
            INVOKE_STATIC = 30,
            INVOKE_VIRTUAL = 31,
            NEW = 40,
            JUMP = 50,
            JUMP_COND = 51,
            BOOL_NEG = 60,
            RETURN = 70;

    private Opcodes() {}

    public static String repr(int[] code,
            RawType[] rawTypeDescTable, TypeDesc[] fullTypeDescTable,
            RawMethod[] methodDescTable, String[] stringTable) {
        assert rawTypeDescTable != null && fullTypeDescTable != null
                && methodDescTable != null && stringTable != null:
            "Printing opcodes requires access to descriptor tables.";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length; ++i) {
            switch (code[i]) {
                case POP:
                    sb.append("POP");
                    break;

                case DUP:
                    sb.append("DUP");
                    break;

                case CONST_TRUE:
                    sb.append("CONST_TRUE");
                    break;

                case CONST_FALSE:
                    sb.append("CONST_FALSE");
                    break;

                case CONST_INT:
                    sb.append("CONST_INT " + code[++i]);
                    break;

                case CONST_CHAR:
                    sb.append(String.format("CONST_CHAR '%c'", code[++i]));
                    break;

                case CONST_DOUBLE: {
                    long i1 = code[i++]; i1 &= 0xffffffffL;
                    long i0 = code[i++]; i0 &= 0xffffffffL;
                    long l = i1 << 32 | i0;
                    double d = Double.longBitsToDouble(l);
                    sb.append(String.format("CONST_DOUBLE %f", d));
                    break;
                }

                case CONST_STRING:
                    sb.append("CONST_STRING \"" + stringTable[code[++i]] + '"');
                    break;

                case CREATE_SEQ:
                    sb.append("CREATE_SEQ elemType=" + fullTypeDescTable[code[++i]] + ", length=" + code[++i]);
                    break;

                case GET_LOCAL:
                    sb.append("GET_LOCAL localIndex=" + code[++i]);
                    break;

                case PUT_LOCAL:
                    sb.append("PUT_LOCAL localIndex=" + code[++i]);
                    break;

                case GET_STATIC_FIELD:
                    sb.append("GET_STATIC_FIELD owner=" + rawTypeDescTable[code[++i]] + " fieldIndex=" + code[++i]);
                    break;

                case PUT_STATIC_FIELD:
                    sb.append("PUT_STATIC_FIELD owner=" + rawTypeDescTable[code[++i]] + " fieldIndex=" + code[++i]);
                    break;

                case GET_INSTANCE_FIELD:
                    sb.append("GET_INSTANCE_FIELD " + stringTable[code[++i]]);
                    break;

                case PUT_INSTANCE_FIELD:
                    sb.append("PUT_INSTANCE_FIELD " + stringTable[code[++i]]);
                    break;

                case INVOKE_STATIC: {
                    int methodTableIdx = code[++i];
                    RawMethod m = methodDescTable[methodTableIdx];
                    TypeDesc[] genArgs = new TypeDesc[m.numGenericParams];
                    for (int j = 0; j < genArgs.length; ++j) {
                        int fullTypeTableIndex = code[++i];
                        genArgs[j] = fullTypeDescTable[fullTypeTableIndex];
                    }
                    sb.append("INVOKE_STATIC " + methodDescTable[methodTableIdx]);
                    sb.append(", genericArgs=").append(Arrays.toString(genArgs));
                    break;
                }

                case INVOKE_VIRTUAL: {
                    int methodTableIdx = code[++i];
                    RawMethod m = methodDescTable[methodTableIdx];
                    TypeDesc[] genArgs = new TypeDesc[m.numGenericParams];
                    for (int j = 0; j < genArgs.length; ++j) {
                        int fullTypeTableIndex = code[++i];
                        genArgs[j] = fullTypeDescTable[fullTypeTableIndex];
                    }
                    sb.append("INVOKE_VIRTUAL " + methodDescTable[methodTableIdx]);
                    sb.append(", genericArgs=").append(Arrays.toString(genArgs));
                    break;
                }

                case NEW:
                    sb.append("NEW " + fullTypeDescTable[code[++i]]);
                    break;

                case JUMP:
                    sb.append(String.format("JUMP %+d", code[++i]));
                    break;

                case JUMP_COND:
                    sb.append(String.format("JUMP_COND %+d", code[++i]));
                    break;

                case BOOL_NEG:
                    sb.append("BOOL_NEG");
                    break;

                case RETURN:
                    sb.append("RETURN");
                    break;

                default:
                    throw new NiftyException("Bad opcode: %d.", code[i]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
