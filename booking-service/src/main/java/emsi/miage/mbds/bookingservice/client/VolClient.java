package emsi.miage.mbds.bookingservice.client;


import emsi.miage.mbds.bookingservice.model.Vol;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vol-service") // Feign utilise le nom enregistré dans Eureka
public interface VolClient {

    // Récupère un vol par son ID depuis le vol-service (port 8081)
    @GetMapping("/vols/{id}")
    Vol getVolById(@PathVariable("id") Long id);

    // Nous allons définir plus tard un endpoint pour mettre à jour les sièges disponibles (PUT/PATCH)
}
