package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        notifyListeners(key, null, "remove");
    }

    @Override
    public V get(K key) {
        V elem = cache.get(key);
        notifyListeners(key, elem, "get");
        return elem;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(K key, V value, String action) {
        listeners.forEach(l -> {
                    try {
                        l.notify(key, value, action);
                    } catch (Exception e) {
                        log.error("Error calling listener notify with params " +
                                "key: {} " +
                                "value: {} " +
                                "action: {}", key, value, action, e);
                    }
                }
        );
    }
}
