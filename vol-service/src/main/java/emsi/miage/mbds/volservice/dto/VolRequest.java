package emsi.miage.mbds.volservice.dto;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class VolRequest {

    @NotBlank(message = "Le numéro de vol est obligatoire")
    private String numeroVol;

    @NotBlank(message = "La ville de départ est obligatoire")
    private String villeDepart;

    @NotBlank(message = "La ville d'arrivée est obligatoire")
    private String villeArrivee;

    @NotNull(message = "L'heure de départ est obligatoire")
    private LocalDateTime heureDepart;

    @Min(value = 0, message = "Le prix ne peut pas être négatif")
    private double prix;

    @Min(value = 0, message = "Le nombre de sièges doit être non négatif")
    private int siegesDisponibles;

    // Getters, Setters, Constructeurs...
}
