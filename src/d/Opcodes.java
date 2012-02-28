package d;

import java.util.Arrays;

import common.*;

import d.ty.desc.TypeDesc;

public final class Opcodes {
    public static final int
            POP = 0,
            DUP = 1,
            // Literals
            CONST_TRUE = 10,
            CONST_FALSE = 11,
            CONST_INT = 12,
            CONST_CHAR = 13,
            CONST_LONG = 14,
            CONST_DOUBLE = 15,
            CONST_STRING = 16,
            CREATE_SEQ = 18,
            // Locals and fields
            GET_LOCAL = 20,
            PUT_LOCAL = 21,
            GET_STATIC_FIELD = 22,
            PUT_STATIC_FIELD = 23,
            GET_INSTANCE_FIELD = 24,
            PUT_INSTANCE_FIELD = 25,
            // Methods
            INVOKE_STATIC = 30,
            INVOKE_VIRTUAL = 31,
            NEW = 40,
            NEW_NO_GENERICS = 41,
            // Control
            JUMP = 50,
            JUMP_COND = 51,
            RETURN = 55,
            RETURN_UNIT = 56,
            // Common operations
            BOOL_NEG = 60,
            INT_NEG = 100,
            INT_ADD = 110,
            INT_SUB = 111,
            INT_MUL = 112,
            INT_DIV = 113,
            INT_MOD = 114,
            INT_IOR = 115,
            INT_XOR = 116,
            INT_AND = 117,
            INT_LSHIFT = 118,
            INT_RSHIFT_UNSIGNED = 119,
            INT_RSHIFT_SIGNED = 120,
            INT_COMPARETO = 121,
            INT_LT = 122,
            INT_LTE = 123,
            INT_GT = 124,
            INT_GTE = 125;

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

                case CONST_LONG: {
                    long i1 = code[++i]; i1 &= 0xffffffffL;
                    long i0 = code[++i]; i0 &= 0xffffffffL;
                    long l = i1 << 32 | i0;
                    sb.append("CONST_LONG " + l);
                    break;
                }

                case CONST_DOUBLE: {
                    long i1 = code[++i]; i1 &= 0xffffffffL;
                    long i0 = code[++i]; i0 &= 0xffffffffL;
                    long l = i1 << 32 | i0;
                    double d = Double.longBitsToDouble(l);
                    sb.append("CONST_DOUBLE " + d);
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

                case NEW_NO_GENERICS:
                    sb.append("NEW_NO_GENERICS " + rawTypeDescTable[code[++i]]);
                    break;

                case JUMP:
                    sb.append(String.format("JUMP %+d", code[++i]));
                    break;

                case JUMP_COND:
                    sb.append(String.format("JUMP_COND %+d", code[++i]));
                    break;

                case RETURN:
                    sb.append("RETURN");
                    break;
                case RETURN_UNIT:
                    sb.append("RETURN_UNIT");
                    break;

                case BOOL_NEG:
                    sb.append("BOOL_NEG");
                    break;
                case INT_NEG:
                    sb.append("INT_NEG");
                    break;
                case INT_ADD:
                    sb.append("INT_ADD");
                    break;
                case INT_SUB:
                    sb.append("INT_SUB");
                    break;
                case INT_MUL:
                    sb.append("INT_MUL");
                    break;
                case INT_DIV:
                    sb.append("INT_DIV");
                    break;
                case INT_MOD:
                    sb.append("INT_MOD");
                    break;
                case INT_IOR:
                    sb.append("INT_IOR");
                    break;
                case INT_XOR:
                    sb.append("INT_XOR");
                    break;
                case INT_AND:
                    sb.append("INT_AND");
                    break;
                case INT_LSHIFT:
                    sb.append("INT_LSHIFT");
                    break;
                case INT_RSHIFT_UNSIGNED:
                    sb.append("INT_RSHIFT_UNSIGNED");
                    break;
                case INT_RSHIFT_SIGNED:
                    sb.append("INT_RSHIFT_SIGNED");
                    break;
                case INT_COMPARETO:
                    sb.append("INT_COMPARETO");
                    break;
                case INT_LT:
                    sb.append("INT_LT");
                    break;
                case INT_LTE:
                    sb.append("INT_LTE");
                    break;
                case INT_GT:
                    sb.append("INT_GT");
                    break;
                case INT_GTE:
                    sb.append("INT_GTE");
                    break;

                default:
                    throw new NiftyException("Bad opcode: %d.", code[i]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
