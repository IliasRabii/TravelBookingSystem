package emsi.miage.mbds.bookingservice.service;

import emsi.miage.mbds.bookingservice.client.HotelClient;
import emsi.miage.mbds.bookingservice.client.VolClient;
import emsi.miage.mbds.bookingservice.model.Hotel;
import emsi.miage.mbds.bookingservice.model.Vol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import feign.FeignException; // NOUVEAU

@Service
public class BookingService {

    private final VolClient volClient;
    private final HotelClient hotelClient;
    private final WebClient webClientPaiement; // Doit être initialisé via un @Bean

    // DTO PaiementDetails doit être déplacé dans un package model ou défini comme suit pour fonctionner:
    // Cette classe est ici pour le contexte, mais devra être implémentée (getters/setters/constructeur)
    private static class PaiementDetails {
        public PaiementDetails(double montant, Long volId, Long hotelId) {}
    }

    @Autowired
    public BookingService(VolClient volClient, HotelClient hotelClient, WebClient.Builder webClientBuilder) {
        this.volClient = volClient;
        this.hotelClient = hotelClient;
        // La bonne pratique est d'initialiser WebClient.Builder dans une classe de configuration @Bean
        this.webClientPaiement = webClientBuilder.baseUrl("http://paiement-service-fictif.com").build();
    }

    public String reserverVoyage(Long volId, Long hotelId) {

        // 1. Vérification des disponibilités via Feign
        // (La gestion des erreurs Feign 404/400 doit idéalement être dans un @ControllerAdvice)
        Vol vol = volClient.getVolById(volId);
        Hotel hotel = hotelClient.getHotelById(hotelId);

        if (vol == null || hotel == null) {
            return "Erreur: Vol ou Hôtel non trouvé.";
        }
        // Cette vérification est redondante mais garde la logique métier
        if (vol.getSiegesDisponibles() < 1 || hotel.getChambresDisponibles() < 1) {
            return "Erreur: Plus de places ou chambres disponibles.";
        }

        double prixTotal = vol.getPrix() + hotel.getPrixNuit();

        // 2. Traitement du Paiement (WebClient) - Simulation
        String confirmation;
        try {
            // Simulation de l'appel réel WebClient
            // Ceci devrait idéalement être non-bloquant, mais on garde .block() pour la simplicité
            webClientPaiement.post()
                    .uri("/process")
                    .bodyValue(new PaiementDetails(prixTotal, volId, hotelId))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            confirmation = "SUCCESS"; // On simule que la réponse est bonne

        } catch (Exception e) {
            // L'exception se produira car l'URL est fictive. Nous traitons cela comme un échec.
            System.err.println("WebClient Erreur : " + e.getMessage());
            return "Erreur lors du traitement du paiement: " + e.getMessage();
        }

        // 3. Logique post-paiement : Décrémenter les stocks
        if (confirmation.equals("SUCCESS")) {
            try {
                // Décrémenter d'une unité
                volClient.decrementerSieges(volId, 1);
                hotelClient.decrementerChambres(hotelId, 1);

                // Ici, insérer la logique d'enregistrement de la réservation finale (BDD)

                return "Réservation complète réussie. Stocks Vol et Hôtel mis à jour.";
            } catch (FeignException e) {
                // L'exception Feign capture les erreurs 400 (Stock insuffisant) ou 404 (ID introuvable)
                if (e.status() == 400) {
                    return "Échec de la réservation: Stock insuffisant. Annulation de paiement nécessaire.";
                }
                return "Erreur Feign lors de la décrémentation des stocks: " + e.getMessage();
            }
        }

        return "Paiement échoué ou non confirmé.";
    }
}