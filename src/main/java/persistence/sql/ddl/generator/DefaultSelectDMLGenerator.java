package persistence.sql.ddl.generator;

import persistence.sql.ddl.EntityTable;

public class DefaultSelectDMLGenerator implements SelectDMLGenerator {
    @Override
    public String generateFindAll(EntityTable entityTable) {
        return "select * from %s;".formatted(entityTable.tableName());
    }

    @Override
    public String generateFindById(EntityTable entityTable) {
        return "select * from %s where %s = ?;".formatted(entityTable.tableName(), entityTable.getNameOfIdColumn());
    }
}
