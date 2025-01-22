package softsuave.placeOrder.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public enum
AdsType {
    ENTERTAINMENT("ENTERTAINMENT"), EDUCATION("EDUCATION"), POLITICS("POLITICS")
    , BUSINESS("BUSINESS"), PERSONAL("PERSONAL"), OTHERS("OTHERS");
    private String value;

    public String getValue(String value) {
        return this.value;
    }

}
