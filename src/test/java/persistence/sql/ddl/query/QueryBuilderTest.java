package persistence.sql.ddl.query;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import persistence.sql.QueryBuilder;
import persistence.sql.ddl.entity.Person;
import persistence.sql.ddl.entity.Person4;

class QueryBuilderTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(QueryBuilderTest.class);

    private final Class<?> entityClass = Person.class;

    private final QueryBuilder queryBuilder = new QueryBuilder();

    @Test
    @DisplayName("@Entity, @Table, @Id, @Column, @Transient 어노테이션을 바탕으로 create 쿼리 만들어보기")
    void createDDL() {
        String ddl = queryBuilder.getCreateTableQuery(entityClass);

        log.debug("DDL: {}", ddl);

        assertThat(ddl)
            .isEqualTo("CREATE TABLE users (id BIGINT AUTO_INCREMENT, nick_name VARCHAR(255), old INTEGER, email VARCHAR(255) UNIQUE NOT NULL)");
    }

    @Test
    @DisplayName("@Entity, @Table(schema), @Id, @Column, @Transient 어노테이션을 바탕으로 create 쿼리 만들어보기")
    void createDDLWithSchema() {
        String ddl = queryBuilder.getCreateTableQuery(Person4.class);

        log.debug("DDL: {}", ddl);

        assertThat(ddl)
            .isEqualTo("CREATE TABLE schema.users (id BIGINT AUTO_INCREMENT, nick_name VARCHAR(255), old INTEGER, email VARCHAR(255) UNIQUE NOT NULL)");
    }

    @Test
    @DisplayName("@Entity, @Table, @Id, @Column, @Transient 어노테이션을 바탕으로 drop 쿼리 만들어보기")
    void buildDropQuery() {
        String dropQuery = queryBuilder.getDropTableQuery(entityClass);

        log.debug("Drop query: {}", dropQuery);

        assertThat(dropQuery).isEqualTo("DROP TABLE users");
    }

    @Test
    @DisplayName("@Entity, @Table(schema), @Id, @Column, @Transient 어노테이션을 바탕으로 drop 쿼리 만들어보기")
    void buildDropQueryWithSchema() {
        String dropQuery = queryBuilder.getDropTableQuery(Person4.class);

        log.debug("Drop query: {}", dropQuery);

        assertThat(dropQuery).isEqualTo("DROP TABLE schema.users");
    }

    @Test
    @DisplayName("클래스 정보와 @Table 어노테이션을 바탕으로 테이블명 가져오기")
    void getTableName() {
        String tableName = queryBuilder.getTableNameFrom(entityClass);

        log.debug("Table name: {}", tableName);

        assertThat(tableName).isEqualTo("users");
    }

    @Test
    @DisplayName("클래스 정보와 @Table(schema) 어노테이션을 바탕으로 테이블명 가져오기")
    void getTableNameWithSchema() {
        String tableName = queryBuilder.getTableNameFrom(Person4.class);

        log.debug("Table name: {}", tableName);

        assertThat(tableName).isEqualTo("schema.users");
    }

    @Test
    @DisplayName("클래스 정보와 @Id, @Column, @Transient 어노테이션을 바탕으로 컬럼 선언문 가져오기")
    void getColumnDefinitionStatement() {
        String columnDefinitionStatement = queryBuilder.getColumnDefinitionsFrom(entityClass);

        log.debug("Column definition statement: {}", columnDefinitionStatement);

        assertThat(columnDefinitionStatement).isEqualTo("id BIGINT AUTO_INCREMENT, nick_name VARCHAR(255), old INTEGER, email VARCHAR(255) UNIQUE NOT NULL");
    }

    @ParameterizedTest(name = "{0} 필드 정보를 바탕으로 컬럼 선언문 가져오기")
    @CsvSource({
        "id,id BIGINT AUTO_INCREMENT",
        "name,nick_name VARCHAR(255)",
        "age,old INTEGER",
        "email,email VARCHAR(255) UNIQUE NOT NULL"
    })
    @DisplayName("클래스의 필드 정보를 바탕으로 컬럼 선언문 가져오기")
    void getColumnDefinitionStatementFromField(
        String fieldName, String expectedColumnDefinitionStatement
    ) throws NoSuchFieldException {
        Field field = entityClass.getDeclaredField(fieldName);

        String actualColumnDefinitionStatement = queryBuilder.getColumnDefinitionFrom(field);

        assertThat(actualColumnDefinitionStatement).isEqualTo(expectedColumnDefinitionStatement);
    }

    @Test
    @DisplayName("요구사항 1 - 위의 정보를 바탕으로 insert 구현해보기")
    void getInsertQuery() {
        Person person = new Person("홍길동", 20, "test@gamil.com");

        String insertQuery = queryBuilder.getInsertQuery(person);

        assertThat(insertQuery).isEqualTo("INSERT INTO users (nick_name, old, email) VALUES ('홍길동', 20, 'test@gamil.com')");
    }

    @Test
    @DisplayName("요구사항 2 - 위의 정보를 바탕으로 모두 조회(findAll) 기능 구현해보기")
    void getSelectAllQuery() {
        // when
        String selectAllQuery = queryBuilder.getSelectAllQuery(Person.class);

        // then
        assertThat(selectAllQuery).isEqualTo("SELECT id, nick_name, old, email FROM users");
    }

    @Test
    @DisplayName("요구사항 3 - 위의 정보를 바탕으로 단건 조회(findById) 기능 구현해보기")
    void getSelectByIdQuery() {
        // when
        String selectByIdQuery = queryBuilder.getSelectByIdQuery(Person.class, 1L);

        // then
        assertThat(selectByIdQuery).isEqualTo("SELECT id, nick_name, old, email FROM users WHERE id = 1");
    }

    @Test
    @DisplayName("요구사항 3 - 위의 정보를 바탕으로 count 쿼리 만들어보기")
    void getSelectCountQuery() {
        // when
        String selectCountQuery = queryBuilder.getSelectCountQuery(Person.class);

        // then
        assertThat(selectCountQuery).isEqualTo("SELECT COUNT(id) FROM users");
    }

    @Test
    @DisplayName("요구사항 4 - 위의 정보를 바탕으로 delete 쿼리 만들어보기")
    void getDeleteAllQuery() {
        // given
        Long givenId = 1L;
        Person givenEntity = new Person(givenId, "홍길동", 20, "test@gmail.com");

        // when
        String deleteAllQuery = queryBuilder.getDeleteAllQuery(Person.class);
        String deleteByIdQuery = queryBuilder.getDeleteByIdQuery(Person.class, givenId);
        String deleteQueryFromEntity = queryBuilder.getDeleteQueryFromEntity(givenEntity);

        // then
        assertThat(deleteAllQuery).isEqualTo("DELETE FROM users");
        assertThat(deleteByIdQuery).isEqualTo("DELETE FROM users WHERE id = 1");
        assertThat(deleteQueryFromEntity).isEqualTo("DELETE FROM users WHERE id = 1");
    }

    @Test
    void getUpdateQuery() {
        // given
        Person givenEntity = new Person(1L, "홍길동", 20, "test@gmail.com");

        // when
        String updateQuery = queryBuilder.getUpdateQuery(givenEntity);

        // then
        assertThat(updateQuery).isEqualTo(
            "UPDATE users SET nick_name = '홍길동', old = 20, email = 'test@gmail.com' WHERE id = 1");
    }
}