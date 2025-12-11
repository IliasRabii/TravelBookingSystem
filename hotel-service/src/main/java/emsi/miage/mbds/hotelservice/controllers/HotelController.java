package emsi.miage.mbds.hotelservice.controllers;

import emsi.miage.mbds.hotelservice.dto.HotelRequest;
import emsi.miage.mbds.hotelservice.dto.HotelResponse;
import emsi.miage.mbds.hotelservice.service.HotelService; // NOUVEAU
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService; // Injection du Service

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public List<HotelResponse> getAllHotels() {
        return hotelService.findAll(); // Délégation complète
    }

    @GetMapping("/{id}")
    public HotelResponse getHotelById(@PathVariable Long id) {
        return hotelService.findById(id); // Délégation complète
    }

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody HotelRequest request) {
        HotelResponse response = hotelService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint de décrémentation pour le Booking-Service
    @PostMapping("/{id}/decrement-rooms")
    public HotelResponse decrementerChambres(@PathVariable Long id, @RequestParam int nombre) {
        // Le contrôleur ne gère plus la logique de stock ni la gestion des exceptions
        return hotelService.decrementerChambres(id, nombre);
    }
}