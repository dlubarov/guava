package ast;

import static util.StringUtils.*;

public class Type {
    public final String name;
    public final Type[] genericArgs;
    
    public Type(String name, Type[] genericArgs) {
        this.name = name;
        this.genericArgs = genericArgs;
    }
    
    public String toString() {
        if (genericArgs.length == 0)
            return name;
        return name + '[' + implode(", ", genericArgs) + ']';
    }
}
