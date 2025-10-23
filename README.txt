Pds - e commerce

Mikroservisi (Eureka, API Gateway, Users, Orders)

Servisi se pokrecu sledecim redosledom: discovery-service ->api-gateway -: users-service -> orders-service

Moguce je pristupiti Eureka dashboard-u na http://localhost:8761, gde mozemo videti sve registrovane korisnike na Eureka server.

Moguce slanje zahteva preko swagger za users service I orders service

Glavni url za pristup preko api-gateway-a na http://localhost:8080/api/**

Pracenje CircuitBreaker stanja je moguce preko actuatora na http://localhost:8082/actuator/circuitbreakers

Portovi:
discovery-service : 8761
 api-gateway : 8080
 users-service : 8081
 orders-service : 8082

