package common;

import java.util.concurrent.atomic.AtomicInteger;

public final class VariableGenerator {
    private static final AtomicInteger counter = new AtomicInteger();

    private VariableGenerator() {}

    public static String randomId(String prefix) {
        return String.format("$%s_%d", prefix, counter.getAndIncrement());
    }

    public static String randomId() {
        return randomId("");
    }
}
