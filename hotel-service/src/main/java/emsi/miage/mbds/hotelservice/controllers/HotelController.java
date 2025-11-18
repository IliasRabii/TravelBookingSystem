package emsi.miage.mbds.hotelservice.controllers;

import emsi.miage.mbds.hotelservice.model.Hotel;
import emsi.miage.mbds.hotelservice.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelRepository hotelRepository;

    @Autowired // Injection de dépendance via constructeur (bonne pratique)
    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable Long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    // Nous ajouterons ici les endpoints pour la réservation plus tard
}
