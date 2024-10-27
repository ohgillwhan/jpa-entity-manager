package persistence.sql.ddl.generator;

import persistence.sql.ddl.EntityTable;
import persistence.sql.ddl.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultUpdateDMLGenerator implements UpdateDMLGenerator {
    @Override
    public String generate(EntityTable entityTable) {
        List<String> columnNames = getColumnNames(entityTable);
        String set = setClause(columnNames);
        String where = whereClause(entityTable);

        return "UPDATE %s SET %s WHERE %s;".formatted(entityTable.tableName(), set, where);
    }

    private String whereClause(EntityTable entityTable) {
        String idColumnName = entityTable.getNameOfIdColumn();

        return "%s = ?".formatted(idColumnName);
    }

    private String setClause(List<String> columnNames) {
        return columnNames.stream().map("%s = ?"::formatted)
            .collect(Collectors.joining(","));
    }

    private List<String> getColumnNames(EntityTable entityTable) {
        return entityTable.getColumnNames();
    }

    private Object getValue(EntityTable entityTable, String columnName, Object object) {
        Field field = entityTable.getFieldByColumnName(columnName);

        Object value = FieldUtils.getValue(field, object);

        if (value == null) {
            return null;
        }

        return "'%s'".formatted(value);
    }
}
