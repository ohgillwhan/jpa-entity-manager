package persistence.entity;

import database.DatabaseServer;
import database.H2;
import domain.Person;
import java.sql.SQLException;
import jdbc.JdbcTemplate;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import persistence.sql.ddl.DdlGenerator;
import persistence.sql.dialect.h2.H2Dialect;

@DisplayName("SimpleEntityManager class 의")
class SimpleEntityManagerTest {

    private DatabaseServer server;

    private JdbcTemplate jdbcTemplate;
    private DdlGenerator ddlGenerator;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws SQLException {
        server = new H2();
        server.start();

        jdbcTemplate = new JdbcTemplate(server.getConnection());
        ddlGenerator = DdlGenerator.getInstance(H2Dialect.getInstance());
        entityManager = SimpleEntityManager.from(jdbcTemplate);
        jdbcTemplate.execute(ddlGenerator.generateCreateQuery(Person.class));
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute(ddlGenerator.generateDropQuery(Person.class));
        server.stop();
    }

    @DisplayName("persist 메서드는")
    @Nested
    class Persist {

        @DisplayName("Person entity를 저장 한다.")
        @Test
        void persistTest_whenInsert() {
            //given
            Person person = createPerson();

            //when
            entityManager.persist(person);

            //then
            Person foundPerson = entityManager.find(person.getClass(), 1L);
            assertAll(
                () -> assertEquals(person.getName(), foundPerson.getName()),
                () -> assertEquals(person.getAge(), foundPerson.getAge()),
                () -> assertEquals(person.getEmail(), foundPerson.getEmail())
            );
        }
    }

    @DisplayName("find 메서드는")
    @Nested
    class Find {

        @DisplayName("Person entity를 검색 할 수 있다.")
        @Test
        void findTest() {
            // given
            Person person = createPerson();
            entityManager.persist(person);

            // when
            Person foundPerson = entityManager.find(Person.class, 1L);

            // then
            assertAll(
                () -> assertEquals(person.getName(), foundPerson.getName()),
                () -> assertEquals(person.getAge(), foundPerson.getAge()),
                () -> assertEquals(person.getEmail(), foundPerson.getEmail())
            );
        }
    }

    @DisplayName("remove 메서드는")
    @Nested
    class Remove {

        @DisplayName("특정 Person을 삭제 할 수 있다.")
        @Test
        void deleteTest() {
            //given
            Person person = createPerson();
            entityManager.persist(person);
            person = entityManager.find(Person.class, 1L);
            //when
            entityManager.remove(person);

            //then
            assertThatThrownBy(() -> entityManager.find(Person.class, 1L))
                .isInstanceOf(RuntimeException.class);
        }
    }

    private Person createPerson() {
        return Person.of("user1", 1, "abc@gtest.com", 1);
    }
}