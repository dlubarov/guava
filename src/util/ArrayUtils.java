package util;

import java.util.*;

public final class ArrayUtils {
    private ArrayUtils() {}

    public static boolean contains(Object[] arr, Object x) {
        for (Object elem : arr)
            if (elem.equals(x))
                return true;
        return false;
    }

    public static boolean hasDuplicates(Object[] arr) {
        return new HashSet<Object>(Arrays.asList(arr)).size() < arr.length;
    }
}
