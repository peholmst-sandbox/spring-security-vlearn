package org.vaadin.peholmst.vlearn.spring.security.service;

import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.vaadin.peholmst.vlearn.spring.security.utils.CurrentUser;

import javax.annotation.PreDestroy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class Diary {

    private final List<Entry> entries = new ArrayList<>();
    private final Set<Listener> listeners = new HashSet<>();
    private final ExecutorService listenerExecutorService;

    Diary() {
        listenerExecutorService = Executors.newFixedThreadPool(10);
    }

    @PreDestroy
    void destroy() {
        listenerExecutorService.shutdown();
    }

    @Secured("ROLE_READER")
    public List<Entry> getEntries() {
        synchronized (entries) {
            return List.copyOf(entries);
        }
    }

    @Secured("ROLE_WRITER")
    public void recordEntry(@NonNull String content) {
        var author = CurrentUser.get().map(Authentication::getName).orElse("Anonymous");
        var timestamp = Instant.now();
        synchronized (entries) {
            entries.add(new Entry(timestamp, author, content));
        }
        Set<Listener> listenersToNotify;
        synchronized (listeners) {
            listenersToNotify = Set.copyOf(listeners);
        }
        // Invoke in separate threads to avoid passing on the security context.
        listenersToNotify.forEach(listener -> listenerExecutorService.submit(listener::onEntryRecorded));
    }

    @NonNull
    public Registration registerListener(@NonNull Listener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
        return () -> {
            synchronized (listeners) {
                listeners.remove(listener);
            }
        };
    }

    @FunctionalInterface
    public interface Listener {
        void onEntryRecorded();
    }

    @FunctionalInterface
    public interface Registration {
        void unregister();
    }
}
