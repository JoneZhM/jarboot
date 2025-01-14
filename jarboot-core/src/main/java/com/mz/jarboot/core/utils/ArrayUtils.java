package com.mz.jarboot.core.utils;

/**
 * @author majianzheng
 * 以下代码基于开源项目Arthas适配修改
 */
public class ArrayUtils {

    /**
     * An empty immutable {@code long} array.
     */
    public static final long[] EMPTY_LONG_ARRAY = new long[0];

    /**
     * <p>Converts an array of object Longs to primitives.</p>
     *
     * <p>This method returns {@code null} for a {@code null} input array.</p>
     *
     * @param array  a {@code Long} array, may be {@code null}
     * @return a {@code long} array, {@code null} if null array input
     * @throws NullPointerException if array content is {@code null}
     */
    public static long[] toPrimitive(final Long[] array) {
        if (array == null) {
            return null; //NOSONAR
        } else if (array.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        final long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].longValue();
        }
        return result;
    }

    private ArrayUtils() {}
}
