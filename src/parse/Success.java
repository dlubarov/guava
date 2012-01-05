package parse;

public class Success<T> {
    public final T value;
    public final int rem;

    public Success(T value, int rem) {
        this.value = value;
        this.rem = rem;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
