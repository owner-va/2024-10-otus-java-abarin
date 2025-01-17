package ru.otus.processors;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.EvenSecondProcessor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EvenSecondProcessorTest {
    @Test
    public void shouldThrowExceptionWhenTimeHaveEvenSeconds() {
        LocalDateTime localDateTime = LocalDateTime.of(1000, 1, 1, 1, 1, 2);
        Message message = new Message.Builder(1L).build();
        EvenSecondProcessor processor = new EvenSecondProcessor(() -> localDateTime);

        assertThrows(RuntimeException.class, () -> processor.process(message));
    }

    @Test
    public void shouldNotThrowExceptionWhenNotHaveEvenSeconds() {
        LocalDateTime localDateTime = LocalDateTime.of(1000, 1, 1, 1, 1, 1);
        Message message = new Message.Builder(1L).build();
        EvenSecondProcessor processor = new EvenSecondProcessor(() -> localDateTime);

        assertDoesNotThrow(() -> processor.process(message));
    }
}
