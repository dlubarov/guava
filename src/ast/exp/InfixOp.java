package ast.exp;

public enum InfixOp {
    IORASS("|="), XORASS("^="), ANDASS("&="),
    LSHASS("<<="), RSHASS(">>="),
    ADDASS("+="), SUBASS("-="),
    MULASS("*="), DIVASS("/="), MODASS("%="),

    IOR("|"), XOR("^"), AND("&"),
    EQ("=="), NEQ("!="),
    LT("<"), GT(">"), LE("<="), GE(">="),
    LSH("<<"), RSH(">>"),
    ADD("+"), SUB("-"),
    MUL("*"), DIV("/"), MOD("%");

    private final String s;

    private InfixOp(String s) {
        this.s = s;
    }

    public String toString() {
        return s;
    }
}
