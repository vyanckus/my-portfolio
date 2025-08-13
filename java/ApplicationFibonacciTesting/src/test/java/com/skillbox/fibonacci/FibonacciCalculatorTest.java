package com.skillbox.fibonacci;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FibonacciCalculatorTest {

    private FibonacciCalculator calculator;

    @BeforeEach
    private void setUp() {
        calculator = new FibonacciCalculator();
    }

    @DisplayName("Test get Fibonacci number")
    @ParameterizedTest
    @CsvSource({
            "3, 2",
            "4, 3",
            "5, 5",
            "10, 55"
    })
    public void testGetFibonacciNumber(int index, int expectedValue) {
        int actualValue = calculator.getFibonacciNumber(index);
        assertEquals(expectedValue, actualValue);
    }

    @DisplayName("Test get Fibonacci number throws exception")
    @ParameterizedTest
    @ValueSource(ints = {0, -10})
    public void testGetFibonacciNumberThrowsException(int index) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> calculator.getFibonacciNumber(index));
        assertEquals("Index should be greater or equal to 1", exception.getMessage());
    }

    @DisplayName("Test get Fibonacci number one")
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void testGetFibonacciNumberOne(int index) {
        int actualValue = calculator.getFibonacciNumber(index);
        assertEquals(1, actualValue);
    }
}
