package softsuave.placeOrder.Services.PaymentFactory;
import softsuave.placeOrder.Model.PaymentDetails;

public interface PaymentModeFactory {
    boolean  payment(PaymentDetails paymentDetails, int LoanTenure) throws Exception;
}
