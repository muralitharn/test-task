package softsuave.placeOrder.Services.RoleBasedImplementation;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface FactortyResponse {
    byte[]   Placeorder(Object object) throws Exception;

}
