package vm;

public final class Opcodes {
    private Opcodes() {}

    public static final int
            POP = 0,
            DUP = 1,
            CONST_TRUE = 2,
            CONST_FALSE = 3,
            CONST_INT = 4,
            GET_LOCAL = 10,
            PUT_LOCAL = 11,
            GET_FIELD = 12,
            PUT_FIELD = 13,
            INVOKE_STATIC = 14,
            INVOKE_VIRTUAL = 15,
            JUMP = 16,
            JUMP_COND = 17,
//            RETURN_VOID = 18,
            RETURN_OBJECT = 19,
            BOOL_NEG = 20;

    private static String repr(int op) {
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
            case INVOKE_STATIC: return "INVOKE_STATIC";
            case INVOKE_VIRTUAL: return "INVOKE_VIRTUAL";
            case JUMP: return "JUMP";
            case JUMP_COND: return "JUMP_COND";
//            case RETURN_VOID: return "RETURN_VOID";
            case RETURN_OBJECT: return "RETURN_OBJECT";
            case BOOL_NEG: return "BOOL_NEG";
            default: return null;
        }
    }
}
