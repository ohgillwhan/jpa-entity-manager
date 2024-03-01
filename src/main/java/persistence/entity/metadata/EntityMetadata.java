package persistence.entity.metadata;

import java.util.List;
import java.util.stream.Collectors;

public class EntityMetadata {
    private String tableName;
    private List<EntityColumn> columns;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumns(List<EntityColumn> columns) {
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<EntityColumn> getColumns() {
        return columns;
    }

    // sql 활용
    public EntityColumn getIdColumn() {
        return columns.stream()
            .filter(EntityColumn::isPrimaryKey)
            .findFirst().orElse(null);
    }

    public List<EntityColumn> getInsertTargetColumns() {
        return columns.stream()
            .filter(column -> !column.isDataAutoGenerated())
            .collect(Collectors.toList());
    }



    @Override
    public String toString() {
        return "EntityMetadata{" +
            "tableName='" + tableName + '\'' +
            ", columns=" + columns +
            '}';
    }
}

