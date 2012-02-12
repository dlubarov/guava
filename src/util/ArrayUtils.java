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

    public static Integer[] boxArray(int[] arr) {
        Integer[] result = new Integer[arr.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = arr[i];
        return result;
    }
}
