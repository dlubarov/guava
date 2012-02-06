package a;

import java.util.Arrays;

public class Type {
    public static final Type[] NONE = new Type[0];

    public final String rawType;
    public final Type[] genericArgs;

    public Type(String rawType, Type[] genericArgs) {
        this.rawType = rawType;
        this.genericArgs = genericArgs;
    }

    public Type(String rawType) {
        this(rawType, NONE);
    }

    public b.Type refine() {
        b.Type[] refinedGenericArgs = new b.Type[genericArgs.length];
        for (int i = 0; i < refinedGenericArgs.length; ++i)
            refinedGenericArgs[i] = genericArgs[i].refine();
        return new b.Type(rawType, refinedGenericArgs);
    }

    @Override
    public String toString() {
        if (genericArgs.length == 0)
            return rawType;
        return rawType + Arrays.toString(genericArgs);
    }
}
