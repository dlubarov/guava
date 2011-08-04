package common;

import java.util.Arrays;

public class RawMethodDesc {
    private final RawTypeDesc owner;
    private final String name;
    public FullTypeDesc[] paramTypes;

    public RawMethodDesc(RawTypeDesc owner, String name, FullTypeDesc[] paramTypes) {
        this.owner = owner;
        this.name = name;
        this.paramTypes = paramTypes;
    }

    public RawMethodDesc(String module, String owner, String name, FullTypeDesc[] paramTypes) {
        this(new RawTypeDesc(module, owner), name, paramTypes);
    }
    
    public String toString() {
        return String.format("%s.%s%s",
                owner, name, Arrays.toString(paramTypes));
    }
}
