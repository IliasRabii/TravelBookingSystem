package emsi.miage.mbds.volservice.repository;


import emsi.miage.mbds.volservice.model.Vol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // <-- C'est cette annotation qui expose l'API REST
public interface VolRepository extends JpaRepository<Vol, Long> {
}