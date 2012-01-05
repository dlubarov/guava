package util;

import java.util.Arrays;

public class Pair<A, B> {
    public final A first;
    public final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Pair<?, ?> that = (Pair<?, ?>) o;
            return first.equals(that.first) && second.equals(that.second);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {first, second});
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", first, second);
    }
}
