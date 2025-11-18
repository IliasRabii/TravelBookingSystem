package emsi.miage.mbds.bookingservice.controllers;


import emsi.miage.mbds.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/{volId}/{hotelId}")
    public String reserver(@PathVariable Long volId, @PathVariable Long hotelId) {
        return bookingService.reserverVoyage(volId, hotelId);
    }
}
