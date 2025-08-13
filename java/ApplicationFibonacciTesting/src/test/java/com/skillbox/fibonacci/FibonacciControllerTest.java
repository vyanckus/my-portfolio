package com.skillbox.fibonacci;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FibonacciControllerTest extends PostgresTestContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FibonacciService fibonacciService;

    @Test
    @DisplayName("Test get number with index greater than 1")
    public void testGetNumberWithIndexGreaterThan1() throws Exception {
        int index = 7;
        int value = 13;
        FibonacciNumber fibonacciNumber = new FibonacciNumber(index, value);
        when(fibonacciService.fibonacciNumber(index)).thenReturn(fibonacciNumber);

        mockMvc.perform(MockMvcRequestBuilders.get("/fibonacci/{index}", index))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.index").value(index))
                .andExpect(jsonPath("$.value").value(value));

        verify(fibonacciService).fibonacciNumber(index);
    }

    @Test
    @DisplayName("Test get number with index less than 1")
    public void testGetNumberWithIndexLessThan1() throws Exception {
        int index = -1;
        when(fibonacciService.fibonacciNumber(index)).thenThrow(
                new IllegalArgumentException("Index should be greater or equal to 1")
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/fibonacci/{index}", index))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Index should be greater or equal to 1"));
    }
}
