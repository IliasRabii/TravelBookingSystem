package emsi.miage.mbds.volservice.dto;


import java.time.LocalDateTime;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class VolResponse {

    private Long id;
    private String numeroVol;
    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime heureDepart;
    private double prix;
    private int siegesDisponibles;
    private String statut; // Champ ajouté : DISPONIBLE ou COMPLET

    // Getters, Setters, Constructeurs...
}