package softsuave.placeOrder.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum paymentMode {

    COD("COD"), DEBITCARD("DEBITCARD"), UPI("UPI"), NETBANKING("NETBANKING"), EMI("EMI");
    private String value;

    public String getValue(String value) {
        return this.value;
    }
}
