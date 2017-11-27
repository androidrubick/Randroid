/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pub.androidrubick.base.utils;


import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains static utility methods pertaining to primitive types and their
 * corresponding wrapper types.
 *
 * @author Kevin Bourrillion
 *
 * @since 1.0
 */
public final class Primitives {
    private Primitives() {
    }

    /**
     * A map from primitive types to their corresponding wrapper types.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;

    /**
     * A map from wrapper types to their corresponding primitive types.
     */
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;

    /**
     * A map from wrapper types to their corresponding primitive types.
     */
    private static final Map<Class<?>, Object> PRIMITIVE_TYPE_DEFVAL;

    // Sad that we can't use a BiMap. :(

    static {
        Map<Class<?>, Class<?>> primToWrap = new HashMap<Class<?>, Class<?>>(16);
        Map<Class<?>, Class<?>> wrapToPrim = new HashMap<Class<?>, Class<?>>(16);
        Map<Class<?>, Object> primVal = new HashMap<Class<?>, Object>(16);

        add(primToWrap, wrapToPrim, primVal, boolean.class, Boolean.class, false);
        add(primToWrap, wrapToPrim, primVal, byte.class, Byte.class, NumberUtils.BYTE_ZERO);
        add(primToWrap, wrapToPrim, primVal, char.class, Character.class, NumberUtils.CHAR_ZERO);
        add(primToWrap, wrapToPrim, primVal, double.class, Double.class, NumberUtils.DOUBLE_ZERO);
        add(primToWrap, wrapToPrim, primVal, float.class, Float.class, NumberUtils.FLOAT_ZERO);
        add(primToWrap, wrapToPrim, primVal, int.class, Integer.class, NumberUtils.INT_ZERO);
        add(primToWrap, wrapToPrim, primVal, long.class, Long.class, NumberUtils.LONG_ZERO);
        add(primToWrap, wrapToPrim, primVal, short.class, Short.class, NumberUtils.SHORT_ZERO);
        add(primToWrap, wrapToPrim, primVal, void.class, Void.class, null);

        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
        PRIMITIVE_TYPE_DEFVAL = Collections.unmodifiableMap(primVal);
    }

    private static void add(Map<Class<?>, Class<?>> forward,
                            Map<Class<?>, Class<?>> backward,
                            Map<Class<?>, Object> primVal,
                            Class<?> key, Class<?> value,
                            Object typeDefVal) {
        forward.put(key, value);
        backward.put(value, key);
        primVal.put(key, typeDefVal);
    }

    /**
     * Returns true if this type is a primitive.
     *
     * @since 1.0
     */
    public static boolean isPrimitive(Type type) {
        return PRIMITIVE_TO_WRAPPER_TYPE.containsKey(type);
    }

    /**
     * Returns {@code true} if {@code type} is one of the nine
     * primitive-wrapper types, such as {@link Integer}.
     *
     * @see Class#isPrimitive
     *
     * @since 1.0
     */
    public static boolean isWrapperType(Type type) {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(type);
    }

    /**
     * Returns the corresponding wrapper type of {@code type} if it is a primitive
     * type; otherwise returns {@code type} itself. Idempotent.
     * <pre>
     *     wrap(int.class) == Integer.class
     *     wrap(Integer.class) == Integer.class
     *     wrap(String.class) == String.class
     * </pre>
     *
     * @since 1.0
     */
    public static <T> Class<T> wrap(Class<T> type) {
        // cast is safe: long.class and Long.class are both of type Class<Long>
        @SuppressWarnings("unchecked")
        Class<T> wrapped = (Class<T>) PRIMITIVE_TO_WRAPPER_TYPE.get(type);
        return (wrapped == null) ? type : wrapped;
    }

    /**
     * Returns the corresponding primitive type of {@code type} if it is a
     * wrapper type; otherwise returns {@code type} itself. Idempotent.
     * <pre>
     *     unwrap(Integer.class) == int.class
     *     unwrap(int.class) == int.class
     *     unwrap(String.class) == String.class
     * </pre>
     *
     * @since 1.0
     */
    public static <T> Class<T> unwrap(Class<T> type) {
        // cast is safe: long.class and Long.class are both of type Class<Long>
        @SuppressWarnings("unchecked")
        Class<T> unwrapped = (Class<T>) WRAPPER_TO_PRIMITIVE_TYPE.get(type);
        return (unwrapped == null) ? type : unwrapped;
    }

    /**
     * return default value of specific type
     *
     * @param type specific type
     * @return default value of this type
     *
     * @since 1.0
     */
    public static Object defValueOf(Class<?> type) {
        return PRIMITIVE_TYPE_DEFVAL.get(type);
    }
}
