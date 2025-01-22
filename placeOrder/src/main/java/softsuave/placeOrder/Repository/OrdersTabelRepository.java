package softsuave.placeOrder.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import softsuave.placeOrder.Model.OrdersTabel;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//@NoRepositoryBean
@Repository("ordersTabelRepository")
public interface OrdersTabelRepository extends JpaRepository<OrdersTabel, UUID> {

    //@Query(nativeQuery = true, value = "SELECT * FROM PlacedOrdersTabel tabel WHERE tabel.creation_date BETWEEN :startDate AND :endDate")
    List<OrdersTabel> findByCreationDate(Date creationDate);

    List<OrdersTabel> findByCustomerId(int customerId );
}
