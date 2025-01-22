package softsuave.placeOrder.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import softsuave.placeOrder.enums.paymentMode;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID  TrasactionID;

    @Column(name="is_Payment_Completed", columnDefinition = "boolean default true")
    private Boolean isPaymentCompleted;

    @Column(name="customer_id")
    @NotNull
    int  CustomerID;

    @Column(name = "payment_type", columnDefinition = "varchar(100) default 'COD'")
    @Enumerated(EnumType.STRING)
    @NotNull
    private paymentMode paymentType;

    @Lob
    @Column(name = "file_data", nullable = true)
    private byte[] fileData;

}


