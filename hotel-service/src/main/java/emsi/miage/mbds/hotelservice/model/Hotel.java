package emsi.miage.mbds.hotelservice.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String ville;
    private double prixNuit;
    private int chambresDisponibles;

    // Constructeur par défaut (Obligatoire pour JPA)
    public Hotel() {
    }

    // Constructeur avec tous les arguments
    public Hotel(Long id, String nom, String ville, double prixNuit, int chambresDisponibles) {
        this.id = id;
        this.nom = nom;
        this.ville = ville;
        this.prixNuit = prixNuit;
        this.chambresDisponibles = chambresDisponibles;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public double getPrixNuit() {
        return prixNuit;
    }

    public void setPrixNuit(double prixNuit) {
        this.prixNuit = prixNuit;
    }

    public int getChambresDisponibles() {
        return chambresDisponibles;
    }

    public void setChambresDisponibles(int chambresDisponibles) {
        this.chambresDisponibles = chambresDisponibles;
    }
}
