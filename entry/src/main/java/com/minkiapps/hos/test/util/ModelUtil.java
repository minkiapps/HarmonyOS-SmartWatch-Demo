package com.minkiapps.hos.test.util;

import java.util.regex.Pattern;

public class ModelUtil {

    private static final Pattern pattern = Pattern.compile(".*00.*");

    public static boolean isChinaModel(final String modelName) {
        return pattern.matcher(modelName).matches();
    }
}
