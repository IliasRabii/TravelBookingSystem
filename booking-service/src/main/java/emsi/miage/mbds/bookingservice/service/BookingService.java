package emsi.miage.mbds.bookingservice.service;

import emsi.miage.mbds.bookingservice.client.HotelClient;
import emsi.miage.mbds.bookingservice.client.VolClient;
import emsi.miage.mbds.bookingservice.dto.BookingRequest;
import emsi.miage.mbds.bookingservice.dto.BookingResponse;
import emsi.miage.mbds.bookingservice.mapper.BookingMapper;
import emsi.miage.mbds.bookingservice.model.Hotel;
import emsi.miage.mbds.bookingservice.model.Vol;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import feign.FeignException;

@Service
public class BookingService {

    private final VolClient volClient;
    private final HotelClient hotelClient;
    private final WebClient webClientPaiement;
    private final BookingMapper bookingMapper;

    private static class PaiementDetails {
        public PaiementDetails(double montant, Long volId, Long hotelId) {}
    }

    @Autowired
    public BookingService(VolClient volClient, HotelClient hotelClient, WebClient.Builder webClientBuilder, BookingMapper bookingMapper) {
        this.volClient = volClient;
        this.hotelClient = hotelClient;
        this.webClientPaiement = webClientBuilder.baseUrl("http://paiement-service-fictif.com").build();
        this.bookingMapper = bookingMapper;
    }

    public BookingResponse reserverVoyage(BookingRequest request) {

        // 1. Appel Feign initial (Vérification des IDs et obtention des détails)
        Vol vol = volClient.getVolById(request.getVolId());
        Hotel hotel = hotelClient.getHotelById(request.getHotelId());

        if (vol == null || hotel == null) {
            throw new RuntimeException("Erreur: Vol ou Hôtel non trouvé.");
        }

        // 2. Simulation du Paiement
        double prixTotal = vol.getPrix() + hotel.getPrixNuit();
        String confirmation = simulatePaiement(prixTotal, request.getVolId(), request.getHotelId());

        if (confirmation.equals("SUCCESS")) {

            // 3. Logique critique protégée par le Circuit Breaker
            String resultMsg = orchestrateBooking(request.getVolId(), request.getHotelId());

            // 4. Utilisation du Mapper pour retourner le DTO final
            return bookingMapper.toResponse(vol, hotel, resultMsg);

        }

        // Cas d'échec de paiement (si non simulé)
        BookingResponse errorResponse = new BookingResponse();
        errorResponse.setStatus("PAYMENT_FAILED");
        errorResponse.setMessage("Paiement échoué ou non confirmé. Statut : " + confirmation);
        return errorResponse;
    }

    // (Reste des méthodes auxiliaires)
    private String simulatePaiement(double prixTotal, Long volId, Long hotelId) {
        System.out.println("--- SIMULATION: Appel au service de paiement fictif ---");
        return "SUCCESS";
    }

    @CircuitBreaker(name = "bookingCircuitBreaker", fallbackMethod = "handleBookingFailure")
    public String orchestrateBooking(Long volId, Long hotelId) {

        Vol vol = volClient.getVolById(volId);
        Hotel hotel = hotelClient.getHotelById(hotelId);

        if (vol.getSiegesDisponibles() < 1 || hotel.getChambresDisponibles() < 1) {
            throw new RuntimeException("Ressources insuffisantes avant décrémentation.");
        }

        try {
            volClient.decrementerSieges(volId, 1);
            hotelClient.decrementerChambres(hotelId, 1);
            return "Réservation complète réussie. Stocks Vol et Hôtel mis à jour.";
        } catch (FeignException e) {
            throw new RuntimeException("Erreur Feign lors de la décrémentation des stocks : " + e.getMessage(), e);
        }
    }

    public String handleBookingFailure(Long volId, Long hotelId, Throwable t) {
        System.err.println("--- CIRCUIT BREAKER ACTIVÉ --- Erreur: " + t.getMessage());

        if (t.getMessage() != null && t.getMessage().contains("Ressources insuffisantes")) {
            return "Échec de la réservation: Stock insuffisant. Annulation de paiement nécessaire.";
        }
        return "Échec critique: Le système de réservation est en panne ou indisponible. Veuillez réessayer plus tard.";
    }
}