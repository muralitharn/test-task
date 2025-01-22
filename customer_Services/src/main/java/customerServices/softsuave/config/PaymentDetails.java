package customerServices.softsuave.config;


import customerServices.softsuave.enums.paymentMode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

    @Enumerated(EnumType.STRING)
    private paymentMode paymentType;

    @Lob
    private byte[] fileData;

    private UUID TrasactionID;
    private Boolean isPaymentCompleted;
    int CustomerID;


}
