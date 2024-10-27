package persistence.sql.ddl.entitypersister;

import persistence.sql.ddl.EntityTable;
import persistence.sql.ddl.generator.DeleteDMLGenerator;
import persistence.sql.ddl.generator.InsertDMLGenerator;
import persistence.sql.ddl.generator.SelectDMLGenerator;
import persistence.sql.ddl.generator.UpdateDMLGenerator;

import java.sql.Connection;

public class DefaultEntityPersister implements EntityPersister {
    private final Connection connection;
    private final EntityTable entityTable;
    private final String queryOfInsert;
    private final String queryOfUpdateById;
    private final String queryOfSelectById;
    private final String queryOfDeleteById;

    public DefaultEntityPersister(Connection connection,
                                  InsertDMLGenerator insertDMLGenerator,
                                  UpdateDMLGenerator updateDMLGenerator,
                                  SelectDMLGenerator selectDMLGenerator,
                                  DeleteDMLGenerator deleteDMLGenerator,
                                  Class<?> clazz) {
        this.connection = connection;
        this.entityTable = EntityTable.from(clazz);
        this.queryOfInsert = insertDMLGenerator.generate(entityTable);
        this.queryOfUpdateById = updateDMLGenerator.generate(entityTable);
        this.queryOfSelectById = selectDMLGenerator.generateFindById(entityTable);
        this.queryOfDeleteById = deleteDMLGenerator.generateDeleteById(entityTable);
    }

    @Override
    public Object insert(Object entity) {
        return null;
    }

    @Override
    public Object delete(Object entity) {
        return null;
    }

    @Override
    public Object update(Object entity) {
        return null;
    }
}
