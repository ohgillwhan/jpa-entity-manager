package persistence.sql.ddl.generator;

import persistence.sql.ddl.EntityTable;

public interface UpdateDMLGenerator {
    String generate(EntityTable entityTable);
}
