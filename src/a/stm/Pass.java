package a.stm;

public class Pass extends Statement {
    public static Pass singleton = new Pass();

    private Pass() {}

    @Override
    public b.stm.Statement refine() {
        return new b.stm.Block();
    }

    @Override
    public String toString() {
        return ";";
    }
}
