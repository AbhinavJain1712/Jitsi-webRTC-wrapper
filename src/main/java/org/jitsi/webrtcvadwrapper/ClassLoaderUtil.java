package org.jitsi.webrtcvadwrapper;

import java.io.InputStream;

public class ClassLoaderUtil {

    private ClassLoaderUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static InputStream getResourceAsStream(String resourcePath) {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = ClassLoaderUtil.class.getClassLoader();
        }
        return classLoader.getResourceAsStream(resourcePath);
    }
}
