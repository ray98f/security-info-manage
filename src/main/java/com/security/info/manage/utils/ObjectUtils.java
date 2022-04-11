package com.security.info.manage.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {

    public static Map<String, Object> objectToMap(Object object){
        Map<String,Object> dataMap = new HashMap<>();
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                dataMap.put(field.getName(), field.get(object) == null ? "" : field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dataMap;
    }

}
