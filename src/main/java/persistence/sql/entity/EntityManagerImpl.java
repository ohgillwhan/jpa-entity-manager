package persistence.sql.entity;

import jdbc.JdbcTemplate;
import persistence.sql.dialect.h2.H2Dialect;
import persistence.sql.dml.SelectQueryBuilder;

import static persistence.sql.entity.RowMapperFactory.createRowMapper;

public class EntityManagerImpl implements EntityManager {

    private final EntityPersister entityPersister;
    private final JdbcTemplate jdbcTemplate;

    public EntityManagerImpl(final EntityPersister entityPersister, final JdbcTemplate jdbcTemplate) {
        this.entityPersister = entityPersister;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public <T> T find(final Class<T> clazz, final Long Id) {
        final SelectQueryBuilder queryBuilder = new SelectQueryBuilder(clazz, new H2Dialect());
        final String findByIdQuery = queryBuilder.createFindByIdQuery(Id);

        return jdbcTemplate.queryForObject(findByIdQuery, createRowMapper(clazz));
    }

    @Override
    public void persist(final Object entity) {
        entityPersister.insert(entity);
    }

    @Override
    public void remove(final Object entity) {
        entityPersister.delete(entity);
    }
}