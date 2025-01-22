package softsuave.placeOrder.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private int paymentID;

    @Column(nullable = true)
    Boolean isPaymentCompleted;


}
