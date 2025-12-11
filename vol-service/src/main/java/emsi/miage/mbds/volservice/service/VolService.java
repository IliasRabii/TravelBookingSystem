package emsi.miage.mbds.volservice.service;


import emsi.miage.mbds.volservice.dto.VolRequest;
import emsi.miage.mbds.volservice.dto.VolResponse;
import emsi.miage.mbds.volservice.mapper.VolMapper;
import emsi.miage.mbds.volservice.model.Vol;
import emsi.miage.mbds.volservice.repository.VolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class VolService {

    private final VolRepository volRepository;
    private final VolMapper volMapper;

    public VolService(VolRepository volRepository, VolMapper volMapper) {
        this.volRepository = volRepository;
        this.volMapper = volMapper;
    }

    public List<VolResponse> findAll() {
        return volMapper.toResponseList(volRepository.findAll());
    }

    public VolResponse findById(Long id) {
        Vol vol = volRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vol non trouvé avec l'ID: " + id
                ));
        return volMapper.toResponse(vol);
    }

    public VolResponse save(VolRequest request) {
        Vol entity = volMapper.toEntity(request);
        Vol savedVol = volRepository.save(entity);
        return volMapper.toResponse(savedVol);
    }

    // Logique de décrémentation essentielle pour le Booking-Service
    public VolResponse decrementerSieges(Long id, int nombre) {
        Vol vol = volRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vol non trouvé"));

        if (nombre <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nombre de sièges doit être positif.");
        }

        if (vol.getSiegesDisponibles() >= nombre) {
            vol.setSiegesDisponibles(vol.getSiegesDisponibles() - nombre);
            Vol updatedVol = volRepository.save(vol);
            return volMapper.toResponse(updatedVol);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock de sièges insuffisant.");
        }
    }
}
