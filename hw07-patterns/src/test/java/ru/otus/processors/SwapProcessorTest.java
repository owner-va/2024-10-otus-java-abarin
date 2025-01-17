package ru.otus.processors;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.SwapProcessor;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwapProcessorTest {

    @Test
    public void shouldSwapFields() {
        Message message = new Message.Builder(1L)
                .field11("11")
                .field12("12")
                .build();
        SwapProcessor processorSwap = new SwapProcessor();
        Message processedMessage = processorSwap.process(message);

        assertEquals(message.getField12(), processedMessage.getField11());
        assertEquals(message.getField11(), processedMessage.getField12());
    }
}