package persistence.sql.ddl.generator;

import persistence.sql.ddl.EntityTable;
import persistence.sql.ddl.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultInsertDMLGenerator implements InsertDMLGenerator {
    @Override
    public String generate(EntityTable entityTable) {
        List<String> columnNames = getColumnNames(entityTable);
        String columns = columnsClause(columnNames);
        String values = valueClause(columnNames);

        return "INSERT INTO %s (%s) values (%s);".formatted(entityTable.tableName(), columns, values);
    }

    private String columnsClause(List<String> columnNames) {
        return String.join(",", columnNames);
    }

    private String valueClause(List<String> columnNames) {
        return columnNames.stream().map(columnName -> "?")
            .collect(Collectors.joining(","));
    }

    private List<String> getColumnNames(EntityTable entityTable) {
        if (entityTable.useAutoGenerateKey()) {
            return entityTable.getColumnNames();
        } else {
            return entityTable.getAllColumnNames();
        }
    }
}
