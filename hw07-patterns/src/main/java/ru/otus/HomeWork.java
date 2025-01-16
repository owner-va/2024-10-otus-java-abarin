package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;
import ru.otus.processor.homework.EvenSecondProcessor;
import ru.otus.processor.homework.SwapProcessor;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {

    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var processors = List.of(new SwapProcessor(), new EvenSecondProcessor(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, ex -> {
        });
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
