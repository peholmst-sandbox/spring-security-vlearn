package org.vaadin.peholmst.vlearn.spring.security.service;

import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

public class ChatMessageEvent extends ApplicationEvent {

    private final String sender;
    private final String message;

    public ChatMessageEvent(@NonNull Object source,
                            @NonNull String sender,
                            @NonNull String message) {
        super(source);
        this.sender = sender;
        this.message = message;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public String getSender() {
        return sender;
    }
}
