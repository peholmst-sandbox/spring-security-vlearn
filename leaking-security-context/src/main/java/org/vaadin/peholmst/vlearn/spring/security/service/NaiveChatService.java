package org.vaadin.peholmst.vlearn.spring.security.service;

import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.vaadin.peholmst.vlearn.spring.security.utils.CurrentUser;

import java.util.HashSet;
import java.util.Set;

@Service
@Secured("ROLE_CHATTER")
public class NaiveChatService {

    private final Set<MessageListener> listeners = new HashSet<>();

    public void sendMessage(@NonNull String message) {
        var sender = CurrentUser.get().map(Authentication::getName).orElse("Anonymous");
        Set<MessageListener> listenersToNotify;
        synchronized (listeners) {
            listenersToNotify = Set.copyOf(listeners);
        }
        listenersToNotify.forEach(listener -> listener.onChatMessage(sender, message));
    }

    @NonNull
    public Registration registerListener(@NonNull MessageListener messageListener) {
        synchronized (listeners) {
            listeners.add(messageListener);
        }
        return () -> {
            synchronized (listeners) {
                listeners.remove(messageListener);
            }
        };
    }

    @FunctionalInterface
    public interface MessageListener {
        void onChatMessage(@NonNull String sender, @NonNull String message);
    }

    @FunctionalInterface
    public interface Registration {
        void unregister();
    }
}
