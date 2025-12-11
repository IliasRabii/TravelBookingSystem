package emsi.miage.mbds.userservice.controllers;


import emsi.miage.mbds.userservice.model.Utilisateur;
import emsi.miage.mbds.userservice.repository.UtilisateurRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UtilisateurController {

    private final UtilisateurRepository repository;

    public UtilisateurController(UtilisateurRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Utilisateur> getAllUsers() {
        return repository.findAll();
    }

    @PostMapping
    public Utilisateur createUser(@RequestBody Utilisateur utilisateur) {
        return repository.save(utilisateur);
    }

    @GetMapping("/{id}")
    public Utilisateur getUser(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }
}
