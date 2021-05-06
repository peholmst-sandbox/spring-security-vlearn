package org.vaadin.peholmst.vlearn.spring.security.utils;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class CurrentUser {

    private CurrentUser() {
    }

    @NonNull
    public static Optional<Authentication> get() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }
}
