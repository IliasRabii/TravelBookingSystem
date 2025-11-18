package emsi.miage.mbds.bookingservice.service;

import emsi.miage.mbds.bookingservice.client.HotelClient;
import emsi.miage.mbds.bookingservice.client.VolClient;
import emsi.miage.mbds.bookingservice.model.Hotel;
import emsi.miage.mbds.bookingservice.model.Vol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BookingService {

    private final VolClient volClient;
    private final HotelClient hotelClient;
    private final WebClient webClientPaiement; // Utilisé pour l'appel externe (paiement)

    @Autowired
    public BookingService(VolClient volClient, HotelClient hotelClient, WebClient.Builder webClientBuilder) {
        this.volClient = volClient;
        this.hotelClient = hotelClient;
        // Initialisation de WebClient pour le service de paiement fictif
        this.webClientPaiement = webClientBuilder.baseUrl("http://paiement-service-fictif.com").build();
    }

    // Cette méthode sera appelée par le contrôleur REST
    public String reserverVoyage(Long volId, Long hotelId) {

        // 1. Vérification des disponibilités via Feign
        Vol vol = volClient.getVolById(volId);
        Hotel hotel = hotelClient.getHotelById(hotelId);

        if (vol == null || hotel == null) {
            return "Erreur: Vol ou Hôtel non trouvé.";
        }

        if (vol.getSiegesDisponibles() < 1 || hotel.getChambresDisponibles() < 1) {
            return "Erreur: Plus de places ou chambres disponibles.";
        }

        double prixTotal = vol.getPrix() + hotel.getPrixNuit();

        // 2. Traitement du Paiement (WebClient)
        // Ceci simule un appel asynchrone ou non-bloquant vers un service externe
        try {
            // Note: Nous utilisons une requête POST simple et bloquante (.block())
            String confirmation = webClientPaiement.post()
                    .uri("/process")
                    .bodyValue(new PaiementDetails(prixTotal, volId, hotelId)) // PaiementDetails est un DTO à créer
                    .retrieve()
                    .bodyToMono(String.class) // Attendre la confirmation du paiement
                    .block();

            // 3. Logique post-paiement (non implémentée ici)
            // Décrémenter les sièges/chambres dans vol-service et hotel-service.

            return "Réservation réussie. Confirmation de paiement: " + confirmation;

        } catch (Exception e) {
            return "Erreur lors du traitement du paiement: " + e.getMessage();
        }
    }

    // Vous aurez besoin d'un DTO pour les détails de paiement
    private static class PaiementDetails {
        // ... Champs pour simuler les données envoyées au service de paiement
        public PaiementDetails(double montant, Long volId, Long hotelId) {}
    }
}