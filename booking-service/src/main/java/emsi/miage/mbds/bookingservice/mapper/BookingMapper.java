package emsi.miage.mbds.bookingservice.mapper;


import emsi.miage.mbds.bookingservice.dto.BookingResponse;
import emsi.miage.mbds.bookingservice.model.Hotel;
import emsi.miage.mbds.bookingservice.model.Vol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface BookingMapper {

    // Méthode pour construire la réponse finale
    @Mapping(target = "dateReservation", expression = "java(LocalDateTime.now())")
    @Mapping(target = "destination", source = "vol.villeArrivee")
    @Mapping(target = "status", constant = "CONFIRMED")
    @Mapping(target = "message", source = "msg")
    @Mapping(target = "prixTotal", expression = "java(vol.getPrix() + hotel.getPrixNuit())")
    BookingResponse toResponse(Vol vol, Hotel hotel, String msg);

    // Vous pouvez créer d'autres méthodes de mapping ici si vous ajoutez une entité "Booking" en base de données plus tard.
}
