package emsi.miage.mbds.hotelservice.service;


import emsi.miage.mbds.hotelservice.dto.HotelRequest;
import emsi.miage.mbds.hotelservice.dto.HotelResponse;
import emsi.miage.mbds.hotelservice.mapper.HotelMapper;
import emsi.miage.mbds.hotelservice.model.Hotel;
import emsi.miage.mbds.hotelservice.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional // Gère les transactions au niveau de la logique métier
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    // Injection du Repository et du Mapper
    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }

    public List<HotelResponse> findAll() {
        return hotelMapper.toResponseList(hotelRepository.findAll());
    }

    public HotelResponse findById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Hôtel non trouvé"
                ));
        return hotelMapper.toResponse(hotel);
    }

    public HotelResponse save(HotelRequest request) {
        Hotel entity = hotelMapper.toEntity(request);
        Hotel savedHotel = hotelRepository.save(entity);
        return hotelMapper.toResponse(savedHotel);
    }

    // Logique de décrémentation déplacée ici
    public HotelResponse decrementerChambres(Long id, int nombre) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hôtel non trouvé"));

        if (nombre <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nombre de chambres doit être positif.");
        }

        if (hotel.getChambresDisponibles() >= nombre) {
            hotel.setChambresDisponibles(hotel.getChambresDisponibles() - nombre);
            Hotel updatedHotel = hotelRepository.save(hotel);
            return hotelMapper.toResponse(updatedHotel);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuffisant.");
        }
    }
}
