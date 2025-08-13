package com.skillbox.fibonacci;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FibonacciServiceTest {
    private final FibonacciRepository fibonacciRepository = Mockito.mock(FibonacciRepository.class);
    private final FibonacciCalculator fibonacciCalculator = Mockito.mock(FibonacciCalculator.class);

    private final FibonacciService fibonacciService = new FibonacciService(fibonacciRepository, fibonacciCalculator);

    @Test
    @DisplayName("Test Fibonacci Number from repository")
    public void testFibonacciNumberFromRepository() {
        int index = 10;
        int value = 55;
        FibonacciNumber fibonacciNumber = new FibonacciNumber(index, value);
        when(fibonacciRepository.findByIndex(index)).thenReturn(Optional.of(fibonacciNumber));
        FibonacciNumber computedNumber = fibonacciService.fibonacciNumber(index);
        assertEquals(value, computedNumber.getValue());
        verify(fibonacciRepository, times(1)).findByIndex(index);
        verifyNoInteractions(fibonacciCalculator);
        verify(fibonacciRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Test Fibonacci Number missing from repository")
    public void testFibonacciNumberMissingFromRepository() {
        int index = 10;
        int value = 55;
        when(fibonacciRepository.findByIndex(index)).thenReturn(Optional.empty());
        when(fibonacciCalculator.getFibonacciNumber(index)).thenReturn(value);
        ArgumentCaptor<FibonacciNumber> fibonacciNumberCaptor = ArgumentCaptor.forClass(FibonacciNumber.class);
        FibonacciNumber computedNumber = fibonacciService.fibonacciNumber(index);
        assertEquals(value, computedNumber.getValue());
        verify(fibonacciCalculator, times(1)).getFibonacciNumber(index);
        verify(fibonacciRepository, times(1)).save(fibonacciNumberCaptor.capture());
        FibonacciNumber capturedFibonacciNumber = fibonacciNumberCaptor.getValue();
        assertEquals(index, capturedFibonacciNumber.getIndex());
        assertEquals(value, capturedFibonacciNumber.getValue());
    }

    @Test
    @DisplayName("Test Fibonacci Number throws exception")
    public void testFibonacciNumberThrowsException() {
        int index = -1;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fibonacciService.fibonacciNumber(index));
        assertEquals("Index should be greater or equal to 1", exception.getMessage());
    }
}
