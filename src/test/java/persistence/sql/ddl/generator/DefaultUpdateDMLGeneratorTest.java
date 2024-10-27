package persistence.sql.ddl.generator;

import org.junit.jupiter.api.Test;
import persistence.sql.ddl.EntityTable;
import persistence.sql.ddl.Person;
import persistence.sql.ddl.generator.DefaultUpdateDMLGenerator;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultUpdateDMLGeneratorTest {
    @Test
    void update_DML을_생성한다() {
        EntityTable entityTable = EntityTable.from(Person.class);
        DefaultUpdateDMLGenerator generator = new DefaultUpdateDMLGenerator();

        String dml = generator.generate(entityTable);

        assertThat(dml).isEqualTo("UPDATE users SET nick_name = ?,old = ?,email = ? WHERE id = ?;");
    }
}