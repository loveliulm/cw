package com.qifu.common.utils;

import com.qifu.common.exception.UtilException;

public final class Asserts {

    private Asserts() {
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new UtilException(message);
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new UtilException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new UtilException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object != null) {
            throw new UtilException(message);
        }
    }
}
