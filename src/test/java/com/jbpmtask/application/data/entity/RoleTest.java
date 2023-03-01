package com.jbpmtask.application.data.entity;

import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class RoleTest {

    @Test
    public void entityGetColumnUsingReflection(){
        Map<String, String> columnMap = new HashMap<>();

        Field[] fields = Role.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                String columnName = field.getAnnotation(Column.class).name();
                columnMap.put(field.getName(), columnName);
            }
        }

        columnMap.forEach((k, v) -> System.out.println("Key:" + k + ", Value:" + v));

    }

}