package hw.testRunner;

import hw.annotations.AfterTest;
import hw.annotations.BeforeTest;
import hw.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {
    private final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    public void run(Class<?> testClass) {
        List<Method> beforeMethod = findAnnotatedMethod(testClass, BeforeTest.class);
        List<Method> afterMethod = findAnnotatedMethod(testClass, AfterTest.class);
        Map<Boolean, Integer> map = new HashMap<>();
        int count = 0;
        for (Method testMethod : testClass.getDeclaredMethods()) {
            if (testMethod.isAnnotationPresent(Test.class)) {
                boolean result = runTestPipelineAndReturnResult(testClass, beforeMethod, afterMethod, testMethod);
                map.put(result, map.getOrDefault(result, 0) + 1);
                count++;
            }
        }

        logger.info("\nTests count: {}\nSuccess: {}\nFailed: {}", count,
                map.getOrDefault(true, 0),
                map.getOrDefault(false, 0));
    }

    private Object getInstance(Class<?> testClass) {
        try {
            return testClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Failed to create test instance: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<Method> findAnnotatedMethod(Class<?> testClass, Class<? extends Annotation> annotationClass) {
        List<Method> methods = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private boolean runTestPipelineAndReturnResult(Class<?> testClass,
                                                   List<Method> beforeMethods,
                                                   List<Method> afterMethods,
                                                   Method testMethod) {
        Object testInstance = getInstance(testClass);
        executeMethods(beforeMethods, testInstance);
        boolean result = executeTest(testMethod, testInstance);
        executeMethods(afterMethods, testInstance);
        logger.info("-----------------------------------------------");
        return result;
    }

    private void executeMethods(List<Method> methods, Object instance) {
        for (Method method : methods) {
            try {
                method.invoke(instance);
            } catch (Exception e) {
                throw new RuntimeException("Error execute " + method.getName() + ". Exception: " + e.getMessage());
            }
        }
    }

    private boolean executeTest(Method testMethod, Object testInstance) {
        try {
            testMethod.invoke(testInstance);
            logger.info("{} passed.", testMethod.getName());
            return true;
        } catch (Exception e) {
            logger.info("{} failed, reason: {}", testMethod.getName(), e.getCause().getMessage());
            return false;
        }
    }
}