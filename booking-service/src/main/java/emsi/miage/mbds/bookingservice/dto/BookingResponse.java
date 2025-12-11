package emsi.miage.mbds.bookingservice.dto;


import java.time.LocalDateTime;

public class BookingResponse {
    private String status;
    private double prixTotal;
    private LocalDateTime dateReservation;
    private String message;

    // Ajoutez aussi les détails sommaires si vous voulez
    private String destination;

    // Constructeurs, Getters et Setters
    public BookingResponse() {}
    public BookingResponse(String status, double prixTotal, LocalDateTime dateReservation, String message, String destination) {
        this.status = status;
        this.prixTotal = prixTotal;
        this.dateReservation = dateReservation;
        this.message = message;
        this.destination = destination;
    }
    // ... Getters/Setters ...
}
