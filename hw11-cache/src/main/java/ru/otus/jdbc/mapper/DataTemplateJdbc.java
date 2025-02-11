package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            EntityClassMetaData<T> entityClassMetaData
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                this::createObjectFromResultSet);
    }

    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                        connection,
                        entitySQLMetaData.getSelectAllSql(),
                        Collections.emptyList(),
                        this::createListFromResultSet)
                .orElse(new ArrayList<>());
    }

    public long insert(Connection connection, T object) {
        return dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getInsertSql(),
                getFieldValues(object));
    }

    public void update(Connection connection, T object) {
        dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getUpdateSql(),
                getFieldValues(object));
    }

    private T createObjectFromResultSet(ResultSet rs) {
        try {
            if (rs.next()) {
                T object = entityClassMetaData.getConstructor().newInstance();
                for (Field field : entityClassMetaData.getAllFields()) {
                    field.setAccessible(true);
                    field.set(object, rs.getObject(field.getName()));
                }
                return object;
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<T> createListFromResultSet(ResultSet rs) {
        List<T> resultList = new ArrayList<>();
        try {
            while (rs.next()) {
                resultList.add(createObjectFromResultSet(rs));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return resultList;
    }

    private List<Object> getFieldValues(T object) {
        return entityClassMetaData.getFieldsWithoutId().stream().map(f -> {
            try {
                f.setAccessible(true);
                return f.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}