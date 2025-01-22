package softsuave.placeOrder.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softsuave.placeOrder.Exception.PlacedOrderException;
import softsuave.placeOrder.Model.PlacedOrdersTabel;
import softsuave.placeOrder.Services.PlacesOrder;

@RestController
@RequestMapping(value = "/placed-orders-api")
public class placedOrders {

    private final PlacesOrder placesOrder;

    public placedOrders(PlacesOrder customerServices) {
        this.placesOrder = customerServices;
    }

    @PostMapping("/save-order")
    public ResponseEntity<Object> saveOrders(@RequestBody PlacedOrdersTabel.orderDto dto) throws Exception {
        byte[] response = placesOrder.saveOrder(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
