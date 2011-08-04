package common;

public class GenericParamDec {
    public final Variance var;
    public final String name;
    
    public GenericParamDec(Variance var, String name) {
        this.name = name;
        this.var = var;
    }
    
    public String toString() {
        return var + name;
    }
}
