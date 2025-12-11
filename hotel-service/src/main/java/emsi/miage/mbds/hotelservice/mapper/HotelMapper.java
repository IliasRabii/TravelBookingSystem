package emsi.miage.mbds.hotelservice.mapper;


import emsi.miage.mbds.hotelservice.dto.HotelRequest;
import emsi.miage.mbds.hotelservice.dto.HotelResponse;
import emsi.miage.mbds.hotelservice.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    // Convertit le DTO d'entrée en entité JPA
    Hotel toEntity(HotelRequest request);

    // Convertit l'entité JPA en DTO de réponse
    @Mapping(target = "statut", expression = "java(entity.getChambresDisponibles() > 0 ? \"DISPONIBLE\" : \"COMPLET\")")
    HotelResponse toResponse(Hotel entity);

    // Convertit une liste d'entités en liste de DTOs de réponse
    List<HotelResponse> toResponseList(List<Hotel> entities);
}
