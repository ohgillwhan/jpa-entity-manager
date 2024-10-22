package persistence.sql.ddl.dialect;

import jakarta.persistence.GenerationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import persistence.sql.ddl.EntityColumn;
import persistence.sql.ddl.SqlJdbcTypes;
import persistence.sql.ddl.dialect.H2Dialect;
import persistence.sql.ddl.exception.NotSupportException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class H2DialectTest {
    static Stream<Arguments> fields() {
        return Stream.of(
            Arguments.of(Long.class, "BIGINT"),
            Arguments.of(String.class, "VARCHAR(255)"),
            Arguments.of(Integer.class, "INTEGER")
        );
    }

    @MethodSource("fields")
    @ParameterizedTest
    void 필드_정의를_구할_수_있다(Class<?> clazz, String definition) {
        H2Dialect h2Dialect = new H2Dialect();
        EntityColumn column = new EntityColumn(null, null, null, false, 255);
        Integer type = SqlJdbcTypes.typeOf(clazz);

        String result = h2Dialect.getColumnDefinition(type, column);

        assertThat(result).isEqualTo(definition);
    }

    @Test
    void 정의되지_않은_클래스의_경우_필드_정의가_실패한다() {
        H2Dialect h2Dialect = new H2Dialect();

        assertThatExceptionOfType(NotSupportException.class)
            .isThrownBy(() -> h2Dialect.getColumnDefinition(Integer.MAX_VALUE, null));
    }

    @Test
    void generationType으로_아이디_채번_전략을_가져올_수_있다() {
        H2Dialect h2Dialect = new H2Dialect();

        String result = h2Dialect.getGenerationDefinition(GenerationType.IDENTITY);

        assertThat(result).isEqualTo("generated by default as identity");
    }

    @Test
    void 지원하지_않는_generationType_의_경우_실패한다() {
        H2Dialect h2Dialect = new H2Dialect();

        assertThatExceptionOfType(NotSupportException.class)
            .isThrownBy(() -> h2Dialect.getGenerationDefinition(GenerationType.AUTO));
    }
}