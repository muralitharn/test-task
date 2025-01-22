//package softsuave.placeOrder.Services.PaymentFactory;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import softsuave.placeOrder.Exception.PlacedOrderException;
//import softsuave.placeOrder.Model.OrdersTabel;
//import softsuave.placeOrder.Model.PaymentDetails;
//import softsuave.placeOrder.Repository.OrdersTabelRepository;
//import softsuave.placeOrder.Services.PlacesOrderServices;
//import softsuave.placeOrder.config.Customer;
//
//@Component
//public class NetbankingModePayment implements PaymentModeFactory {
//
//    @Autowired
//    PlacesOrderServices PlacesOrder;
//
//    @Autowired
//    OrdersTabelRepository odersTabelRepo;
//
//    @Override
//    public boolean  payment(PaymentDetails paymentDetails) {
//        try {
//            Customer Customer = PlacesOrder.makeRequestWithHeaders(paymentDetails.getCustomerID());
//            if (Customer == null) {
//                throw new PlacedOrderException("invalid customer id");}
//            OrdersTabel ordersTabel= odersTabelRepo.findByCustomerId(paymentDetails.getCustomerID());
//            return createWordData(ordersTabel,new StringBuilder("5.5"),Customer.getCustomerName());
//        }
//        catch (Exception message) {
//            message.getMessage();
//        }
//        return new byte[0];
//    }
//
//    public static byte[] createWordData(OrdersTabel placedOrdersTabel, StringBuilder wordFileSize, String password) {
//
//        return null; }
//}
