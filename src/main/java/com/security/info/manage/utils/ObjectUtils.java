package com.security.info.manage.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {

    public static Map<String, Object> objectToMap(Object object) {
        Map<String, Object> dataMap = new HashMap<>();
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

    public static Map<String, Double> findNeighDrugstore(double longitude, double latitude, double distance) {
        double minlat = 0;
        double maxlat = 0;
        double minlng = 0;
        double maxlng = 0;

        double r = 6371;
        double dlng = 2 * Math.asin(Math.sin(distance / (2 * r)) / Math.cos(longitude * Math.PI / 180));
        dlng = dlng * 180 / Math.PI;
        double dlat = distance / r;
        dlat = dlat * 180 / Math.PI;
        if (dlng < 0) {
            minlng = longitude + dlng;
            maxlng = longitude - dlng;
        } else {
            minlng = longitude - dlng;
            maxlng = longitude + dlng;
        }
        if (dlat < 0) {
            minlat = latitude + dlat;
            maxlat = latitude - dlat;
        } else {
            minlat = latitude - dlat;
            maxlat = latitude + dlat;
        }
        Map<String, Double> params = new HashMap<>(4);
        params.put("minLat", minlat);
        params.put("maxLat", maxlat);
        params.put("minLng", minlng);
        params.put("maxLng", maxlng);
        return params;
    }

}
