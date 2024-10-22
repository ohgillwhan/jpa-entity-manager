package persistence.sql.ddl;

import persistence.model.EntityColumn;
import persistence.model.EntityFactory;
import persistence.model.EntityTable;
import persistence.sql.dialect.Dialect;

import java.util.List;
import java.util.stream.Collectors;

public class DdlQueryBuilder {
    private final Dialect dialect;

    public DdlQueryBuilder(Dialect dialect) {
        this.dialect = dialect;
    }

    public String buildDropTableQuery(Class<?> entityClass) {
        String DROP_TABLE_QUERY = "DROP TABLE";

        EntityTable table = EntityFactory.createEmptySchema(entityClass);
        String tableNameQuoted = dialect.getIdentifierQuoted(table.getName());

        return dialect.checkIfExitsBeforeDropTable()
                ? DROP_TABLE_QUERY + " " + "IF EXISTS" + " " + tableNameQuoted
                : DROP_TABLE_QUERY + " " + tableNameQuoted;
    }

    public String buildCreateTableQuery(Class<?> entityClass) {
        String CREATE_TABLE_FORMAT = "CREATE TABLE %s (%s);";

        EntityTable table = EntityFactory.createEmptySchema(entityClass);

        return String.format(
                CREATE_TABLE_FORMAT,
                dialect.getIdentifierQuoted(table.getName()),
                String.join(", ", getColumnsDdl(table), getPrimaryDdl(table))
        );
    }

    private String getPrimaryDdl(EntityTable table) {
        List<String> columnNames = table.getPrimaryColumns().stream()
                .map(EntityColumn::getName)
                .collect(Collectors.toList());

        if (columnNames.isEmpty()) {
            throw new IllegalArgumentException("CANNOT QUERY TABLE WITHOUT PK. name = " + table.getName());
        }
        return getPrimaryKeyPhrase(columnNames);
    }

    private String getPrimaryKeyPhrase(List<String> columnNames) {
        String quotedColumnNames = columnNames.stream()
                .map(dialect::getIdentifierQuoted)
                .collect(Collectors.joining(", "));

        return String.format("PRIMARY KEY (%s)", quotedColumnNames);
    }

    private String getColumnsDdl(EntityTable table) {
        return table.getColumns()
                .stream().map(this::getColumnDdl)
                .collect(Collectors.joining(", "));
    }

    private String getColumnDdl(EntityColumn column) {
        String nameQuoted = dialect.getIdentifierQuoted(column.getName());
        String dataType = dialect.getDataTypeFullName(column.getType(), column.getLength());
        String nullPhrase = dialect.getNullDefinitionPhrase(column.isNullable());

        if (column.isAutoGeneratedIdentity()) {
            return getAutoGeneratedIdentityColumnDdl(nameQuoted, dataType, nullPhrase);
        }
        return nameQuoted + " " + dataType + " " + nullPhrase;
    }

    private String getAutoGeneratedIdentityColumnDdl(String name, String dataType, String nullPhrase) {
        StringBuilder builder = new StringBuilder();

        builder.append(name).append(" ").append(dataType);

        if (dialect.shouldSpecifyNotNullOnIdentity()) {
            builder.append(" ").append(nullPhrase);
        }
        builder.append(" ").append(dialect.getAutoGeneratedIdentityPhrase());

        return builder.toString();
    }
}
