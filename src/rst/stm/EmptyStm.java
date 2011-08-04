package rst.stm;

public class EmptyStm extends Statement {
    public static final EmptyStm INST = new EmptyStm();
    
    private EmptyStm() {}
    
    public String toString() {
        return ";";
    }
}
