package emsi.miage.mbds.bookingservice.model;


import java.time.LocalDateTime;

public class Vol {

    private Long id;
    private String numeroVol;
    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime heureDepart;
    private double prix;
    private int siegesDisponibles;

    // 1. Constructeur par défaut (Obligatoire pour Feign/JSON)
    public Vol() {
    }

    // 2. Constructeur avec tous les arguments
    public Vol(Long id, String numeroVol, String villeDepart, String villeArrivee, LocalDateTime heureDepart, double prix, int siegesDisponibles) {
        this.id = id;
        this.numeroVol = numeroVol;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.heureDepart = heureDepart;
        this.prix = prix;
        this.siegesDisponibles = siegesDisponibles;
    }

    // 3. Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public LocalDateTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalDateTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getSiegesDisponibles() {
        return siegesDisponibles;
    }

    public void setSiegesDisponibles(int siegesDisponibles) {
        this.siegesDisponibles = siegesDisponibles;
    }
}