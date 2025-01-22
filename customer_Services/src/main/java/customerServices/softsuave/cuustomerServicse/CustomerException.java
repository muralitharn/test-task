package customerServices.softsuave.cuustomerServicse;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerException extends Exception {
    String message;

    public CustomerException(String message) {
        super(message);
    }

}
