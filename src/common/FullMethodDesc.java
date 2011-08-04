package common;

import java.util.Arrays;

import static util.StringUtils.implode;

public class FullMethodDesc {
    public final RawTypeDesc owner;
    public final String name;
    public final FullTypeDesc[] genericArgs;
    public final FullTypeDesc[] params;

    public FullMethodDesc(RawTypeDesc type, String name, FullTypeDesc[] genericArgs, FullTypeDesc[] params) {
        this.owner = type;
        this.name = name;
        this.genericArgs = genericArgs;
        this.params = params;
    }

    public String toString() {
        return String.format("%s.%s%s(%s)",
                owner, name,
                Arrays.toString(genericArgs),
                implode(", ", params));
    }
}
