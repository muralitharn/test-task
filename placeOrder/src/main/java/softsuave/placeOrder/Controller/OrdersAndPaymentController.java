package softsuave.placeOrder.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softsuave.placeOrder.Exception.PlacedOrderException;
import softsuave.placeOrder.Model.OrdersTabel;
import softsuave.placeOrder.Model.PaymentDetails;
import softsuave.placeOrder.Services.PlacesOrderServices;

@RestController
@RequestMapping(value = "/placed-orders-api")
public class OrdersAndPaymentController {

    @Autowired
    PlacesOrderServices placesOrder;



    @PostMapping("/save-orders")
    public ResponseEntity<String> saveOrders(@RequestBody OrdersTabel.orderDto dto) throws Exception {
        String response = placesOrder.saveOrder(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/payment/GenerateBills")
    public  ResponseEntity<String>  GenerateBills(@RequestBody PaymentDetails paymentDetails,@RequestParam(required=false,defaultValue = "1",name="loanTenure") int LoanTenure) throws Exception {
        boolean response =   placesOrder.GenerateBill(paymentDetails,LoanTenure);
        String responseMessage= response ?"payment done successfully ":"payment_failed_Raja";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/get/paymentDetails/")
    public ResponseEntity<PaymentDetails> getPaymentDetails(@RequestParam (required=true,name="customerId")int customerId) throws PlacedOrderException {
        PaymentDetails response=  placesOrder.getPaymentDetails(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
