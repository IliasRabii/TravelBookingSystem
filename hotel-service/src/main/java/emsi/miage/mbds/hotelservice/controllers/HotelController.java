package emsi.miage.mbds.hotelservice.controllers; // Note: j'ai corrigé le package en "controller"

import emsi.miage.mbds.hotelservice.model.Hotel;
import emsi.miage.mbds.hotelservice.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    // 1. GET ALL (Correct)
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    // 2. GET BY ID (Amélioration: retourne 404 si non trouvé)
    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Hôtel non trouvé avec l'ID: " + id
                ));
    }

    // 3. POST CREATE (Correct)
    @PostMapping
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    // 4. LOGIQUE ESSENTIELLE: Décrémentation des chambres pour le Booking-Service
    @PostMapping("/{id}/decrement-rooms")
    public ResponseEntity<Hotel> decrementerChambres(@PathVariable Long id, @RequestParam int nombre) {

        return (ResponseEntity<Hotel>) hotelRepository.findById(id).map(hotel -> {

            if (nombre <= 0) {
                return ResponseEntity.badRequest().build(); // Nombre invalide
            }

            if (hotel.getChambresDisponibles() >= nombre) {
                // Décrémentation
                hotel.setChambresDisponibles(hotel.getChambresDisponibles() - nombre);

                // Sauvegarde et réponse OK (200)
                return ResponseEntity.ok(hotelRepository.save(hotel));
            } else {
                // Stock insuffisant (400 Bad Request)
                return ResponseEntity.badRequest().body(hotel);
            }
        }).orElse(ResponseEntity.notFound().build()); // Hôtel non trouvé (404)
    }
}