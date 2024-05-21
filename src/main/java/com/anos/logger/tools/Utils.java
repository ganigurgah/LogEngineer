package com.anos.logger.tools;

/**
 * @author gani.gurgah
 */
public final class Utils {

    public boolean isStringNothing(String str) {
        return str == null || str.trim().length() <= 0;
    }
}
