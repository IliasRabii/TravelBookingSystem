package emsi.miage.mbds.volservice.controllers; // Attention au nom du package (controller sans 's' de préférence)

import emsi.miage.mbds.volservice.dto.VolRequest;
import emsi.miage.mbds.volservice.dto.VolResponse;
import emsi.miage.mbds.volservice.service.VolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vols")
public class VolController {

    private final VolService volService;

    public VolController(VolService volService) {
        this.volService = volService;
    }

    @GetMapping
    public List<VolResponse> getAllVols() {
        return volService.findAll();
    }

    @GetMapping("/{id}")
    public VolResponse getVolById(@PathVariable Long id) {
        return volService.findById(id);
    }

    @PostMapping
    public ResponseEntity<VolResponse> createVol(@RequestBody VolRequest request) {
        return new ResponseEntity<>(volService.save(request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/decrement-seats")
    public VolResponse decrementerSieges(@PathVariable Long id, @RequestParam int nombre) {
        return volService.decrementerSieges(id, nombre);
    }
}