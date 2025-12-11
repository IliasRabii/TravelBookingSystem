package emsi.miage.mbds.userservice.repository;


import emsi.miage.mbds.userservice.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
}
