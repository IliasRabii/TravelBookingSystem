package emsi.miage.mbds.volservice.controllers;


import emsi.miage.mbds.volservice.model.Vol;
import emsi.miage.mbds.volservice.repository.VolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/vols")
public class VolController {

    private final VolRepository volRepository;

    @Autowired
    public VolController(VolRepository volRepository) {
        this.volRepository = volRepository;
    }

    // 1. Amélioration: GET pour tous les vols (en plus de Spring Data REST)
    @GetMapping
    public List<Vol> getAllVols() {
        return volRepository.findAll();
    }

    // 2. Amélioration: GET pour un seul vol
    @GetMapping("/{id}")
    public Vol getVolById(@PathVariable Long id) {
        return volRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vol non trouvé avec l'ID: " + id
                ));
    }

    // 3. Endpoint POST pour la création (nécessaire si vous n'utilisez pas Data REST pour le POST)
    @PostMapping
    public Vol createVol(@RequestBody Vol vol) {
        return volRepository.save(vol);
    }


    // 4. ESSENTIEL: Logique de décrémentation des sièges pour le Booking-Service
    @PostMapping("/{id}/decrement-seats")
    public ResponseEntity<Vol> decrementerSieges(@PathVariable Long id, @RequestParam int nombre) {

        // Utilisation de .map pour gérer l'Optional et la logique
        return (ResponseEntity<Vol>) volRepository.findById(id).map(vol -> {
            if (vol.getSiegesDisponibles() >= nombre && nombre > 0) {
                // Décrémentation
                vol.setSiegesDisponibles(vol.getSiegesDisponibles() - nombre);

                // Sauvegarde et réponse OK (200)
                return ResponseEntity.ok(volRepository.save(vol));
            } else if (vol.getSiegesDisponibles() < nombre) {
                // Si stock insuffisant, retourne 400 Bad Request (Stock Insuffisant)
                return ResponseEntity.badRequest().body(vol);
            } else {
                // Si nombre est invalide (ex: 0 ou négatif)
                return ResponseEntity.badRequest().build();
            }
        }).orElse(ResponseEntity.notFound().build()); // Si Vol non trouvé (404)
    }
}
