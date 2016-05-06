package com.openu.util;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NamedLock {

    private static ConcurrentHashMap<String, Lock> map = new ConcurrentHashMap<>();

    public static void lock(String id) {
        Lock lock = new ReentrantLock();
        Optional.ofNullable(map.putIfAbsent(id, lock)).orElse(lock).lock();
    }

    public static void unlock(String id) {
        map.get(id).unlock();
    }
}
