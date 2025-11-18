package emsi.miage.mbds.hotelservice.repository;


import emsi.miage.mbds.hotelservice.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
