package emsi.miage.mbds.hotelservice.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HotelRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @NotNull(message = "Le prix par nuit est obligatoire")
    @Min(value = 10, message = "Le prix minimum est 10")
    private double prixNuit;

    @Min(value = 0, message = "Les chambres ne peuvent pas être négatives")
    private int chambresDisponibles;

    // Getters, Setters, Constructeurs...
}
