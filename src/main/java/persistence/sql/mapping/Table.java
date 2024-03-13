package persistence.sql.mapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Table {

    private final String name;

    private final Map<String, Column> columns;

    private final PrimaryKey primaryKey;

    public Table(String name) {
        this.name = name;
        this.columns = new LinkedHashMap<>();
        this.primaryKey = new PrimaryKey();
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return this.columns.values().stream().map(Column::clone).collect(Collectors.toList());
    }

    public Column getColumn(final String columnName) {
        return this.columns.get(columnName).clone();
    }

    public PrimaryKey getPrimaryKey() {
        return this.primaryKey;
    }

    public boolean hasPrimaryKey() {
        return this.getPrimaryKey() != null;
    }

    public void addColumn(final Column column) {
        this.columns.put(column.getName(), column);

        if (column.isPk()) {
            primaryKey.addColumn(column);
        }
    }

    public void addColumns(final List<Column> columns) {
        columns.forEach(this::addColumn);
    }
}