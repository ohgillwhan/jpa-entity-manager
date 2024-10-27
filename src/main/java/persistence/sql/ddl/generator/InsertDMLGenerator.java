package persistence.sql.ddl.generator;

import persistence.sql.ddl.EntityTable;

public interface InsertDMLGenerator {
    String generate(EntityTable entityTable);
}
