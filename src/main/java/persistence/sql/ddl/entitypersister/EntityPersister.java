package persistence.sql.ddl.entitypersister;

public interface EntityPersister {
    Object insert(Object entity);

    Object delete(Object entity);

    Object update(Object entity);
}
