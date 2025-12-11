package emsi.miage.mbds.bookingservice.controllers;

import emsi.miage.mbds.bookingservice.dto.BookingRequest;
import emsi.miage.mbds.bookingservice.dto.BookingResponse;
import emsi.miage.mbds.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class BookingController {

    private final BookingService bookingService;

    // Injection de dépendance via constructeur (bonne pratique)
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> reserver(@Valid @RequestBody BookingRequest request) {

        // Le contrôleur délègue et gère la validation de l'entrée (@Valid)
        BookingResponse response = bookingService.reserverVoyage(request);

        // Retourne le DTO de réponse structurée
        return ResponseEntity.ok(response);
    }
}