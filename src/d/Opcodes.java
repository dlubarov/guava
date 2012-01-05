package d;

import common.NiftyException;

public final class Opcodes {
    public static final int
            POP = 0,
            DUP = 1,
            CONST_TRUE = 10,
            CONST_FALSE = 11,
            CONST_INT = 12,
            CONST_CHAR = 13,
            CONST_STRING = 14,
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

    public static String repr(int[] code) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length; ++i)
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
                case CONST_STRING:
                    sb.append("CONST_STRING " + code[++i]);
                    break;
                case GET_LOCAL:
                    sb.append("GET_LOCAL " + code[++i]);
                    break;
                case PUT_LOCAL:
                    sb.append("PUT_LOCAL " + code[++i]);
                    break;
                case GET_STATIC_FIELD:
                    sb.append("GET_STATIC_FIELD " + code[++i]);
                    break;
                case PUT_STATIC_FIELD:
                    sb.append("PUT_STATIC_FIELD " + code[++i]);
                    break;
                case GET_INSTANCE_FIELD:
                    sb.append("GET_INSTANCE_FIELD " + code[++i]);
                    break;
                case PUT_INSTANCE_FIELD:
                    sb.append("PUT_INSTANCE_FIELD " + code[++i]);
                    break;
                case INVOKE_STATIC:
                    sb.append("INVOKE_STATIC " + code[++i]);
                    break;
                case INVOKE_VIRTUAL:
                    sb.append("INVOKE_VIRTUAL " + code[++i]);
                    break;
                case NEW:
                    sb.append("NEW " + code[++i]);
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
                    throw new NiftyException("bad opcode: %d", code[i]);
            }
        return sb.toString();
    }
}
