package emsi.miage.mbds.volservice.mapper;


import emsi.miage.mbds.volservice.dto.VolRequest;
import emsi.miage.mbds.volservice.dto.VolResponse;
import emsi.miage.mbds.volservice.model.Vol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface VolMapper {

    // Convertit le DTO d'entrée en entité JPA
    Vol toEntity(VolRequest request);

    // Convertit l'entité JPA en DTO de réponse
    @Mapping(target = "statut", expression = "java(entity.getSiegesDisponibles() > 0 ? \"DISPONIBLE\" : \"COMPLET\")")
    VolResponse toResponse(Vol entity);

    // Convertit une liste d'entités en liste de DTOs de réponse
    List<VolResponse> toResponseList(List<Vol> entities);
}
