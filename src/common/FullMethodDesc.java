package common;

import java.util.Arrays;

import static util.StringUtils.implode;

public class FullMethodDesc {
    public final RawTypeDesc owner;
    public final String name;
    public final FullTypeDesc[] genericArgs;
    public final FullTypeDesc[] paramTypes;

    public FullMethodDesc(RawTypeDesc owner, String name, FullTypeDesc[] genericArgs, FullTypeDesc[] paramTypes) {
        this.owner = owner;
        this.name = name;
        this.genericArgs = genericArgs;
        this.paramTypes = paramTypes;
    }

    public String toString() {
        return String.format("%s.%s%s(%s)",
                owner, name,
                Arrays.toString(genericArgs),
                implode(", ", paramTypes));
    }
}
