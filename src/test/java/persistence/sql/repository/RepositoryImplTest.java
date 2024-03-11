package persistence.sql.repository;

import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.repository.Repository;
import persistence.repository.RepositoryImpl;
import persistence.sql.db.H2Database;
import persistence.sql.dml.exception.InvalidDeleteNullPointException;
import persistence.sql.entity.exception.RemoveEntityException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class RepositoryImplTest extends H2Database {

    private Person person1;
    private Person person2;

    private Repository<Person, Long> personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new RepositoryImpl<>(entityManager, Person.class);
        personRepository.deleteAll();

        person1 = new Person(1L, "박재성", 10, "jason");
        person2 = new Person(2L, "이동규", 11, "cu");

        personRepository.save(person1);
        personRepository.save(person2);
    }

    @DisplayName("전체 목록을 반환한다.")
    @Test
    void findAllTest() {
        List<Person> result = personRepository.findAll();

        assertThat(result).containsExactly(person1, person2);
    }

    @DisplayName("아이디값에 해당하는 값을 반환한다.")
    @Test
    void findByIdTest() {
        Optional<Person> person = personRepository.findById(1L);

        assertThat(person.get()).isEqualTo(person1);
    }

    @DisplayName("해당하는 아이디에 해당하는 Person 값을 삭제한다.")
    @Test
    void deleteIdTest() {
        personRepository.deleteById(1L);

        assertThatThrownBy(() -> personRepository.findById(1L))
                .isInstanceOf(RemoveEntityException.class)
                .hasMessage("삭제된 엔티티입니다.");
    }

    @DisplayName("디비에 없는값을 제거시 에러를 반환한다.")
    @Test
    void isNotExistsDeleteTest() {
        assertThatThrownBy(() -> personRepository.deleteById(3L))
                .isInstanceOf(InvalidDeleteNullPointException.class)
                .hasMessage("값이 존재하지 않아 삭제가 불가능합니다.");
    }
}