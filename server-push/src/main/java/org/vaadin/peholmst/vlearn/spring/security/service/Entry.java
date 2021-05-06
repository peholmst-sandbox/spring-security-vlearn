package org.vaadin.peholmst.vlearn.spring.security.service;

import org.springframework.lang.NonNull;

import java.time.Instant;

public class Entry {

    private final Instant created;
    private final String author;
    private final String content;

    public Entry(@NonNull Instant created, @NonNull String author, @NonNull String content) {
        this.created = created;
        this.author = author;
        this.content = content;
    }

    @NonNull
    public Instant getCreated() {
        return created;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    @NonNull
    public String getContent() {
        return content;
    }
}
