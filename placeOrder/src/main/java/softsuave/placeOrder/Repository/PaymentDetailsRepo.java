package softsuave.placeOrder.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softsuave.placeOrder.Model.PaymentDetails;
import java.util.UUID;

@Repository
public interface PaymentDetailsRepo extends JpaRepository<PaymentDetails, UUID> {

    @Query(value = "SELECT * FROM payment_details WHERE customer_id = :customerID", nativeQuery = true)
    PaymentDetails findByCustomerID(@Param("customerID") int customerID);

}
