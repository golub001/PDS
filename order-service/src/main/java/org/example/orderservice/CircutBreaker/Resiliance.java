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

    // --- FEIGN poziv po emailu (vraca ResponseEntity)
    @Retry(name = "userServiceRetry")
    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "getUserFallback")
    public ResponseEntity<?> fetchUserByEmailOrThrow(String email) {
        log.info("Poziv user-service-a (Feign) email={}", email);
        return userFeign.GetId(email);
    }

    public ResponseEntity<?> getUserFallback(String email, Throwable t) {
        log.warn("CB fallback pri trazenju korisnika sa email={} cause={}", email, t.toString());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Users service unavailable - fallback for email=" + email);
    }

    // --- FEIGN poziv po ID (vraca UserDto direktno)
    @Retry(name = "userServiceRetry")
    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "getUserByIdFallback")
    public UserDto fetchUserByIdOrThrow(Integer id) {
        log.info("Poziv user-service-a (Feign) id={}", id);
        return userFeign.GetById(id); // vraća direktno UserDto
    }

    public UserDto getUserByIdFallback(Integer id, Throwable t) {
        log.warn("CB fallback pri trazenju korisnika id={} cause={}", id, t.toString());
        // fallback može da vrati "default" UserDto sa praznim poljima
        UserDto fallbackUser = new UserDto();
        fallbackUser.setFirstName("Unknown User (CB Fallback)");
        fallbackUser.setLastName("Unknown User (CB Fallback)");
        fallbackUser.setEmail("unknown@example.com");
        fallbackUser.setPhone("Unkown Phone");
        return fallbackUser;
    }
}
