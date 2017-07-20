package org.ar4j.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationUtils {
    public static String getAnnotationValue(AnnotatedElement clazz, Class annotationClass, String annotationField) {
        if (clazz.isAnnotationPresent(annotationClass)) {
            Annotation annotation = clazz.getAnnotation(annotationClass);
            try {
                Method m = annotation.getClass().getDeclaredMethod(annotationField);
                return (String) m.invoke(annotation);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return "";
            }
        } else
            return "";
    }
}
