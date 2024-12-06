package hw;

import hw.aop.LoggingProxy;

public class Main {
    public static void main(String[] args) {
        TestLoggingInterface testWithLogging = LoggingProxy.createProxy(new TestLogging());
        testWithLogging.calculation(1);
        testWithLogging.calculation(1, 2);
        testWithLogging.calculation(1, 2, 3);
    }
}