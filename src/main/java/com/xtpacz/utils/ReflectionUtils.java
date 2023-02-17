package com.xtpacz.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {
    
    /** 
    * @Description: inject 
    * @Param: [field, obj, value]
    * @return: void
    * @Author: Xtpacz
    * @Date: 2023/2/15
    */
    public static void injectField(Field field, Object obj, Object value) throws IllegalAccessException {
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        }
    }
}
