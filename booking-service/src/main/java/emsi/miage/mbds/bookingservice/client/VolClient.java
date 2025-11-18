package emsi.miage.mbds.bookingservice.client;

import emsi.miage.mbds.bookingservice.model.Vol;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // NOUVEAU
import org.springframework.web.bind.annotation.RequestParam; // NOUVEAU

@FeignClient(name = "vol-service")
public interface VolClient {

    @GetMapping("/vols/{id}")
    Vol getVolById(@PathVariable("id") Long id);

    // ESSENTIEL POUR L'ÉTAPE 6: Nouvelle méthode pour décrémenter les sièges
    @PostMapping("/vols/{id}/decrement-seats")
    Vol decrementerSieges(@PathVariable("id") Long id, @RequestParam("nombre") int nombre);
}