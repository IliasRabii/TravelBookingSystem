package emsi.miage.mbds.bookingservice.client;


import emsi.miage.mbds.bookingservice.model.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hotel-service") // Feign utilise le nom enregistré dans Eureka
public interface HotelClient {

    // Récupère un hôtel par son ID depuis le hotel-service (port 8082)
    @GetMapping("/hotels/{id}")
    Hotel getHotelById(@PathVariable("id") Long id);
}
