package com.skillbox.fibonacci;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FibonacciRepositoryTest extends PostgresTestContainerInitializer {

    @Autowired
    FibonacciRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EntityManager entityManager;

    @AfterEach
    public void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM fibonacci_number");
    }

    @Test
    @DisplayName("Test new Fibonacci number is saved")
    public void testNewFibonacciNumberIsSaved() {
        int index = 10;
        int value = 55;
        FibonacciNumber number = new FibonacciNumber(index, value);
        repository.save(number);
        entityManager.flush();
        entityManager.detach(number);

        List<FibonacciNumber> actual = jdbcTemplate.query(
                "SELECT * FROM fibonacci_number",
                (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"), rs.getInt("value"))
        );
        assertEquals(1, actual.size());
        assertEquals(index, actual.get(0).getIndex());
        assertEquals(value, actual.get(0).getValue());
    }

    @Test
    @DisplayName("Test find Fibonacci number by index")
    public void testFindFibonacciNumberByIndex() {
        int index = 3;
        int value = 2;
        FibonacciNumber number = new FibonacciNumber(index, value);
        repository.save(number);
        entityManager.flush();
        entityManager.detach(number);

        List<FibonacciNumber> actual = jdbcTemplate.query(
                "SELECT * FROM fibonacci_number WHERE index = " + index,
                (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"), rs.getInt("value"))
        );
        assertEquals(1, actual.size());
        assertEquals(index, actual.get(0).getIndex());
        assertEquals(value, actual.get(0).getValue());
    }

    @Test
    @DisplayName("Test repeat Fibonacci number")
    public void testRepeatFibonacciNumber() {
        int index = 6;
        int initialValue = 5; //Намеренно указано неверное значение
        int updatedValue = 8;

        FibonacciNumber number = new FibonacciNumber(index, initialValue);
        repository.save(number);
        entityManager.flush();
        entityManager.detach(number);

        List<FibonacciNumber> actual = jdbcTemplate.query(
                "SELECT * FROM fibonacci_number WHERE index = " + index,
                (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"), rs.getInt("value"))
        );
        assertEquals(1, actual.size());
        assertEquals(initialValue, actual.get(0).getValue());

        FibonacciNumber updatedNumber = new FibonacciNumber(index, updatedValue);
        assertDoesNotThrow(() -> {
            repository.save(updatedNumber);
            entityManager.flush();
            entityManager.detach(updatedNumber);
        });

        List<FibonacciNumber> updatedActual = jdbcTemplate.query(
                "SELECT * FROM fibonacci_number WHERE index = " + index,
                (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"), rs.getInt("value"))
        );
        assertEquals(1, updatedActual.size());
        assertEquals(updatedValue, updatedActual.get(0).getValue());
    }
}
