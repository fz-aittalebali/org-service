package org.dxc.orgservice.shared.adapter.out.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.dxc.orgservice.shared.application.ports.out.IUserService;
import org.dxc.orgservice.shared.query.dtos.UserPageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserServiceAdapter implements IUserService {

    private static final Logger log       = LoggerFactory.getLogger(UserServiceAdapter.class);
    private static final String RESILIENCE = "user-service_getUsersByTeamIds";

    private final UserServiceOpenfeignClient client;

    public UserServiceAdapter(UserServiceOpenfeignClient client) {
        this.client = client;
    }

    @Override
    @CircuitBreaker(name = RESILIENCE, fallbackMethod = "getUsersByTeamIdsFallback")
    @Retry(name = RESILIENCE)
    @RateLimiter(name = RESILIENCE)
    public UserPageResponse getUsersByTeamIds(List<UUID> teamIds, String role, int page, int size) {
        return client.getUsersByTeamIds(teamIds, role, page, size);
    }

    public UserPageResponse getUsersByTeamIdsFallback(
            List<UUID> teamIds, String role, int page, int size, Throwable ex) {
        log.warn("user-service unavailable for teamIds {} — returning empty page. Cause: {}",
                teamIds, ex.getMessage());
        return new UserPageResponse(List.of(), page, size, 0, 0, true);
    }
}
