package com.siadsistemas.projeto_siad.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

        var user = SecurityContextHolder.getContext().getAuthentication();

        if (user != null && user.isAuthenticated()) {
            return Optional.of(user.getName());
        }

        return Optional.empty();
    }
}
