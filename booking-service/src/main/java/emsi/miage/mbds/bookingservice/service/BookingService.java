package emsi.miage.mbds.bookingservice.service;

import emsi.miage.mbds.bookingservice.client.HotelClient;
import emsi.miage.mbds.bookingservice.client.VolClient;
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

    // Classe PaiementDetails déplacée ici pour la compilation
    private static class PaiementDetails {
        public PaiementDetails(double montant, Long volId, Long hotelId) {
            // Corps vide ou avec affectation des champs pour simuler le DTO
        }
    }

    // Pour une bonne pratique, WebClient.Builder devrait être configuré dans une classe @Configuration
    @Autowired
    public BookingService(VolClient volClient, HotelClient hotelClient, WebClient.Builder webClientBuilder) {
        this.volClient = volClient;
        this.hotelClient = hotelClient;
        // Initialisation de WebClient pour le service de paiement fictif
        this.webClientPaiement = webClientBuilder.baseUrl("http://paiement-service-fictif.com").build();
    }

    public String reserverVoyage(Long volId, Long hotelId) {

        // La première étape (vérification d'ID et calcul du prix) est faite ici,
        // avant le disjoncteur, car elle est rapide et ne nécessite pas de protection immédiate.

        Vol vol = volClient.getVolById(volId);
        Hotel hotel = hotelClient.getHotelById(hotelId);

        if (vol == null || hotel == null) {
            return "Erreur: Vol ou Hôtel non trouvé.";
        }

        // Simuler le paiement pour obtenir une confirmation
        String confirmation = simulatePaiement(vol.getPrix() + hotel.getPrixNuit(), volId, hotelId);

        if (confirmation.equals("SUCCESS")) {
            // L'orchestration critique est protégée par le Circuit Breaker
            return orchestrateBooking(volId, hotelId);
        }

        return "Paiement échoué ou non confirmé. Statut : " + confirmation;
    }

    // --- NOUVELLE MÉTHODE : SIMULATION DU PAIEMENT ---
    private String simulatePaiement(double prixTotal, Long volId, Long hotelId) {
        try {
            // Simplification : nous retournons directement SUCCESS pour éviter l'échec de résolution DNS
            // Si vous avez réussi à configurer une URL de test simple, utilisez-la ici.

            // --- DÉBUT DE LA SIMULATION ---
            System.out.println("--- SIMULATION: Appel au service de paiement fictif ---");
            return "SUCCESS";
            // --- FIN DE LA SIMULATION ---

            // Code WebClient original:
            /*
            webClientPaiement.post()
                    .uri("/process")
                    .bodyValue(new PaiementDetails(prixTotal, volId, hotelId))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return "SUCCESS";
            */

        } catch (Exception e) {
            System.err.println("WebClient Erreur : " + e.getMessage());
            return "WebClient Failed";
        }
    }

    // --- NOUVELLE MÉTHODE : LOGIQUE D'ORCHESTRATION PROTÉGÉE ---

    // Protège les appels Feign (dépendances des services Vol/Hôtel)
    @CircuitBreaker(name = "bookingCircuitBreaker", fallbackMethod = "handleBookingFailure")
    public String orchestrateBooking(Long volId, Long hotelId) {

        // Vérification des stocks de dernière minute (redondante, mais sécurisante)
        Vol vol = volClient.getVolById(volId);
        Hotel hotel = hotelClient.getHotelById(hotelId);

        if (vol.getSiegesDisponibles() < 1 || hotel.getChambresDisponibles() < 1) {
            // Si les stocks sont insuffisants, lever une exception pour déclencher le fallback
            throw new RuntimeException("Ressources insuffisantes avant décrémentation.");
        }

        try {
            // Décrémenter d'une unité
            volClient.decrementerSieges(volId, 1);
            hotelClient.decrementerChambres(hotelId, 1);

            // Ici, enregistrer la Réservation finale (BDD)

            return "Réservation complète réussie. Stocks Vol et Hôtel mis à jour.";
        } catch (FeignException e) {
            // Problème de connexion ou de stock du service cible
            throw new RuntimeException("Erreur Feign lors de la décrémentation des stocks : " + e.getMessage(), e);
        }
    }

    // --- NOUVELLE MÉTHODE : FALLBACK (Gestion de la panne) ---
    // Cette méthode est appelée si orchestrateBooking échoue ou s'il y a un timeout/erreur réseau
    public String handleBookingFailure(Long volId, Long hotelId, Throwable t) {
        System.err.println("--- CIRCUIT BREAKER ACTIVÉ --- Le disjoncteur est ouvert ou l'appel a échoué.");
        System.err.println("Cause de l'échec : " + t.getMessage());

        // IMPORTANT : Si le paiement est réussi (dans reserverVoyage), mais que cette étape échoue,
        // vous devez implémenter ici la logique de compensation (Annulation de paiement ou log d'erreur).

        if (t.getMessage() != null && t.getMessage().contains("Ressources insuffisantes")) {
            return "Échec de la réservation: Stock insuffisant. Annulation de paiement nécessaire.";
        }

        return "Échec critique: Le système de réservation est en panne ou indisponible. Veuillez réessayer plus tard.";
    }
}