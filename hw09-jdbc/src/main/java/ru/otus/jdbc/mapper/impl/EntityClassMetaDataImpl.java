package ru.otus.jdbc.mapper.impl;

import ru.otus.crm.annotations.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String className;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> fields;

    public EntityClassMetaDataImpl(Class<T> tClass) throws NoSuchMethodException {
        this.className = tClass.getSimpleName();
        this.constructor = tClass.getConstructor();
        this.idField = Arrays.stream(tClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("idField is not found"));
        this.fields = List.copyOf(Arrays.asList(tClass.getDeclaredFields()));
    }

    @Override
    public String getName() {
        return this.className;
    }

    @Override
    public Constructor<T> getConstructor() {
        return this.constructor;
    }

    @Override
    public Field getIdField() {
        return this.idField;
    }

    @Override
    public List<Field> getAllFields() {
        return this.fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.fields.stream()
                .filter(f -> !f.equals(this.idField))
                .toList();
    }
}
