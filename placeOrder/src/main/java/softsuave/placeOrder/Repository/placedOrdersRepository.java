package softsuave.placeOrder.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softsuave.placeOrder.Model.PlacedOrdersTabel;


import java.util.Date;
import java.util.List;
import java.util.UUID;
@Repository
public interface placedOrdersRepository extends JpaRepository<PlacedOrdersTabel, UUID> {

    //@Query(nativeQuery = true, value = "SELECT * FROM PlacedOrdersTabel tabel WHERE tabel.creation_date BETWEEN :startDate AND :endDate")
    List<PlacedOrdersTabel> findByCreationDate(Date creationDate);
}
