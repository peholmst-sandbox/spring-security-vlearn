package org.vaadin.peholmst.vlearn.spring.security;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class VaadinAwareSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    private final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    @NonNull
    public SecurityContext getContext() {
        var context = contextHolder.get();
        if (context == null) {
            context = getFromVaadinSession().orElseGet(() -> {
                var newCtx = createEmptyContext();
                contextHolder.set(newCtx);
                return newCtx;
            });
        }
        return context;
    }

    @NonNull
    private Optional<SecurityContext> getFromVaadinSession() {
        var session = VaadinSession.getCurrent();
        if (session == null) {
            return Optional.empty();
        }
        var securityContext = session.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (securityContext instanceof SecurityContext) {
            return Optional.of((SecurityContext) securityContext);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void setContext(@NonNull SecurityContext securityContext) {
        contextHolder.set(requireNonNull(securityContext));
    }

    @Override
    @NonNull
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
