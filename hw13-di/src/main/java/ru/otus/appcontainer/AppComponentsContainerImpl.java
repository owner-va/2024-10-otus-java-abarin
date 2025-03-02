package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        var filteredMethods = getAppComponentMethodsSortedByOrder(configClass.getMethods());
        var configInstance = getInstance(configClass);
        filteredMethods.forEach(m -> {
            var annotation = m.getAnnotation(AppComponent.class);
            var name = annotation.name();
            if (appComponentsByName.containsKey(name)) {
                throw new RuntimeException("Key already exist");
            }
            try {
                Object object;
                if (m.getParameterCount() == 0) {
                    object = m.invoke(configInstance);
                } else {
                    var argumentsForMethod = getArgumentsForMethod(m);
                    object = m.invoke(configInstance, argumentsForMethod);
                }
                appComponents.add(object);
                appComponentsByName.put(name, object);
            } catch (Exception e) {
                throw new RuntimeException("Exception invoke method: " + e);
            }
        });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private List<Method> getAppComponentMethodsSortedByOrder(Method[] methods) {
        return Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> {
                    AppComponent annotation = m.getAnnotation(AppComponent.class);
                    return annotation.order();
                }))
                .toList();
    }

    private Object getInstance(Class<?> configClass) {
        Object configInstance;
        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Not create new instance: ", e);
        }
        return configInstance;
    }

    private Object[] getArgumentsForMethod(Method m) {
        return Arrays.stream(m.getParameterTypes())
                .map(this::getComponentByType)
                .toArray();
    }

    private Object getComponentByType(Class<?> type) {
        var list = appComponents.stream().filter(type::isInstance).toList();
        if (list.isEmpty()) {
            throw new RuntimeException("Not found by type: " + type);
        }
        if (list.size() > 1) {
            throw new RuntimeException("Context contains more than one component of type:" + type);
        }
        return list.getFirst();
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return componentClass.cast(getComponentByType(componentClass));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (!appComponentsByName.containsKey(componentName)) {
            throw new RuntimeException("Not found by key: " + componentName);
        }
        return (C) appComponentsByName.get(componentName);
    }
}
