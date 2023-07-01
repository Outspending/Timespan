package me.outspending.timespan;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

public class ExpirableHashMap<K, V> {

    private final HashMap<K, Entry<V>> data = new HashMap<>();

    public void put(K key, V value, long seconds) {
        long expirationTime = System.currentTimeMillis() + (seconds * 1000);
        Entry<V> entry = new Entry<>(value, expirationTime);
        data.put(key, entry);
    }

    public V get(K key) {
        Entry<V> entry = data.get(key);
        if (entry != null && System.currentTimeMillis() < entry.expirationTime) {
            return entry.value;
        } else {
            data.remove(key);
            return null;
        }
    }

    public boolean containsKey(K key) {
        Entry<V> entry = data.get(key);
        if (entry != null && System.currentTimeMillis() < entry.expirationTime) {
            return true;
        } else {
            data.remove(key);
            return false;
        }
    }

    public void remove(K key) {
        data.remove(key);
    }

    public void clear() {
        data.clear();
    }

    public void forEach(@NotNull BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<K, Entry<V>> entry : data.entrySet()) {
            K k;
            Entry<V> v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v.value);
        }
    }

    public HashMap<K, V> getExpired() {
        HashMap<K, V> expired = new HashMap<>();
        if (data.isEmpty())
            return expired;

        data.forEach((key, value) -> {
            if (System.currentTimeMillis() > value.expirationTime)
                expired.put(key, value.value);
        });
        return expired;
    }

    public Entry<V> getLastExpirable() {
        if (data.isEmpty())
            return null;

        Entry<V> last = null;
        for (Entry<V> entry : data.values()) {
            if (last == null || entry.expirationTime > last.expirationTime)
                last = entry;
        }
        return last;
    }

    public Entry<V> getFirstExpirable() {
        if (data.isEmpty())
            return null;

        Entry<V> first = null;
        for (Entry<V> entry : data.values()) {
            if (first == null || entry.expirationTime < first.expirationTime)
                first = entry;
        }
        return first;
    }

    public Entry<V> getEntry(K key) {
        return data.get(key);
    }

    public Set<K> keySet() {
        return data.keySet();
    }

    public Collection<Entry<V>> values() {
        return data.values();
    }

    public Set<Map.Entry<K, Entry<V>>> entrySet() {
        return data.entrySet();
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    private static class Entry<V> {
        private final V value;
        private final long expirationTime;

        private Entry(V value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }
    }
}
