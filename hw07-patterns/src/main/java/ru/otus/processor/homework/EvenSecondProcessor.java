package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class EvenSecondProcessor implements Processor {

    private final Supplier<LocalDateTime> dateTime;

    public EvenSecondProcessor(Supplier<LocalDateTime> dateTimeProvider) {
        this.dateTime = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int second = dateTime.get().getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException("Exception throw because time have even second " + second);
        }
        return message;
    }
}