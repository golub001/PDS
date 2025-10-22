package org.example.orderservice.CircutBreaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.Dto.UserDto;
import org.example.orderservice.Feign.UserFeign;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class Resiliance {

    private final UserFeign userFeign;

    @Retry(name = "userServiceRetry")
    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "getUserFallback")
    public ResponseEntity<?> fetchUserByEmailOrThrow(String email) {
        log.info("Poziv user-service-a (Feign) email=" + email);
        return userFeign.GetId(email); // Feign poziv
    }

    public UserDto getUserFallback(String email, Throwable t) {
        log.warn("CB fallback pri trazenju korisnika sa email-om = " + email + ", cause=" + t.toString());
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "User service trenutno nije dostupan (CB fallback)",
                t
        );
    }
}
