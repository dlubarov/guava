package vm;

public final class Opcodes {
    private Opcodes() {}

    public static final int
            POP = 0,
            DUP = 1,
            CONST_TRUE = 10,
            CONST_FALSE = 11,
            CONST_INT = 12,
            GET_LOCAL = 20,
            PUT_LOCAL = 21,
            GET_FIELD = 22,
            PUT_FIELD = 23,
            GET_STATIC_FIELD = 24,
            PUT_STATIC_FIELD = 25,
            INVOKE_STATIC = 30,
            INVOKE_VIRTUAL = 31,
            NEW = 40,
            JUMP = 50,
            JUMP_COND = 51,
            BOOL_NEG = 60,
            RETURN = 70;

    public static String repr(int op) {
        switch (op) {
            case POP: return "POP";
            case DUP: return "DUP";
            case CONST_TRUE: return "CONST_TRUE";
            case CONST_FALSE: return "CONST_FALSE";
            case CONST_INT: return "CONST_INT";
            case GET_LOCAL: return "GET_LOCAL";
            case PUT_LOCAL: return "PUT_LOCAL";
            case GET_FIELD: return "GET_FIELD";
            case PUT_FIELD: return "PUT_FIELD";
            case GET_STATIC_FIELD: return "GET_STATIC_FIELD";
            case PUT_STATIC_FIELD: return "PUT_STATIC_FIELD";
            case INVOKE_STATIC: return "INVOKE_STATIC";
            case INVOKE_VIRTUAL: return "INVOKE_VIRTUAL";
            case NEW: return "NEW";
            case JUMP: return "JUMP";
            case JUMP_COND: return "JUMP_COND";
            case BOOL_NEG: return "BOOL_NEG";
            case RETURN: return "RETURN";
            default: return null;
        }
    }
}
