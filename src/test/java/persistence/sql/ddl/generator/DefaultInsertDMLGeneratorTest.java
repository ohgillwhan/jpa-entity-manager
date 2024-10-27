package persistence.sql.ddl.generator;

import org.junit.jupiter.api.Test;
import persistence.sql.ddl.EntityTable;
import persistence.sql.ddl.Person;
import persistence.sql.ddl.entity.PersonNotIdentity;
import persistence.sql.ddl.generator.DefaultInsertDMLGenerator;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultInsertDMLGeneratorTest {
    @Test
    void 아이디가_자동생성인_insert_DML을_생성한다() {
        EntityTable entityTable = EntityTable.from(Person.class);
        DefaultInsertDMLGenerator generator = new DefaultInsertDMLGenerator();

        String dml = generator.generate(entityTable);

        assertThat(dml).isEqualTo("INSERT INTO users (nick_name,old,email) values (?,?,?);");
    }

    @Test
    void 아이디가_자동생성이_아닌_insert_DML을_생성한다() {
        EntityTable entityTable = EntityTable.from(PersonNotIdentity.class);
        DefaultInsertDMLGenerator generator = new DefaultInsertDMLGenerator();

        String dml = generator.generate(entityTable);

        assertThat(dml).isEqualTo("INSERT INTO users (id,nick_name,old,email) values (?,?,?,?);");
    }
}
