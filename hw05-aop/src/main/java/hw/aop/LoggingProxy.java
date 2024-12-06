package hw.aop;

import hw.annotations.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class LoggingProxy implements InvocationHandler {
    private final Object tClass;
    private final Set<Method> methods;

    private static final Logger logger = LoggerFactory.getLogger(LoggingProxy.class);

    public LoggingProxy(Object tClass, Method[] methods) {
        this.tClass = tClass;

        this.methods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Log.class))
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T tClass) {
        return (T) Proxy.newProxyInstance(
                tClass.getClass().getClassLoader(),
                tClass.getClass().getInterfaces(),
                new LoggingProxy(tClass, tClass.getClass().getMethods())
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method targetMethod = tClass.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (methods.contains(targetMethod)) {
            logger.info("executed method:{} , param{}", method.getName(), Arrays.toString(args));
        }
        return method.invoke(tClass, args);
    }
}
