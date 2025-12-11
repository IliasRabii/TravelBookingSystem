package emsi.miage.mbds.bookingservice.dto;


import org.antlr.v4.runtime.misc.NotNull;

// Si vous n'utilisez pas Lombok, générez les Getters/Setters/Constructeurs
public class BookingRequest {

    @NotNull(message = "L'ID du vol est obligatoire")
    private Long volId;

    @NotNull(message = "L'ID de l'hôtel est obligatoire")
    private Long hotelId;

    // Getters et Setters
    public Long getVolId() { return volId; }
    public void setVolId(Long volId) { this.volId = volId; }
    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
}