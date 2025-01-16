package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class SwapProcessor implements Processor {

    @Override
    public Message process(Message message) {
        String field11 = String.copyValueOf(message.getField11().toCharArray());
        String field12 = String.copyValueOf(message.getField12().toCharArray());

        return message.toBuilder()
                .field11(field12)
                .field12(field11).build();
    }
}
