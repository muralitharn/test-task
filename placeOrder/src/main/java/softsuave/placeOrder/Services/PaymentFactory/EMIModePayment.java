package softsuave.placeOrder.Services.PaymentFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import softsuave.placeOrder.Exception.PlacedOrderException;
import softsuave.placeOrder.Model.AdsCostTabel;
import softsuave.placeOrder.Model.OrdersTabel;
import softsuave.placeOrder.Model.PaymentDetails;
import softsuave.placeOrder.Repository.OrdersTabelRepository;
import softsuave.placeOrder.Repository.PaymentDetailsRepo;
import softsuave.placeOrder.Services.DocumentProvider;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.List;

import static softsuave.placeOrder.Services.DocumentProvider.createExcelData;
import static softsuave.placeOrder.Services.DocumentProvider.wordTypeDataForEMI;

@Service
public class EMIModePayment implements PaymentModeFactory {

    private OrdersTabelRepository ordersTabelRepository;

    private PaymentDetailsRepo paymentDetailsRepo;

    private RestTemplate restTemplate;

    private DocumentProvider documentMaker;

    @Autowired
    public EMIModePayment(PaymentDetailsRepo paymentDetailsRepo, RestTemplate restTemplate, OrdersTabelRepository ordersTabelRepository
    ,DocumentProvider documentMaker) {
        this.paymentDetailsRepo = paymentDetailsRepo;
        this.restTemplate = restTemplate;
        this.ordersTabelRepository = ordersTabelRepository;
        this.documentMaker= documentMaker;
    }


    int totalPrinciple = 0;
    @Override
    public boolean  payment(PaymentDetails paymentDetails,int LoanTenure) throws Exception {
        try {
            //  EMI = (P X R/12) X [(1+R/12) ^N] / [(1+R/12) ^N-1].   formula for emi calculation....
            List<OrdersTabel> orders = ordersTabelRepository.findByCustomerId(paymentDetails.getCustomerID());
            for (OrdersTabel placed : orders) {
                for (AdsCostTabel ads : placed.getAdsCost()) {
                    totalPrinciple += getPrincipal(ads, placed.getDays());
                }
            }
//           PaymentDetails payDetails = paymentDetailsRepo.findByCustomerID(paymentDetails.getCustomerID());
            double annualIntrestRate = 6.1;
            int EMI = (int) calculateEMI(totalPrinciple, annualIntrestRate, LoanTenure);
            paymentDetails.setFileData(wordTypeDataForEMI(orders, new StringBuilder("3.5"), orders.get(0).getCustomerName(), orders.get(0).getTotalCost(), (int) annualIntrestRate, LoanTenure, EMI));
            paymentDetailsRepo.save(paymentDetails);
            documentMaker.saveToSystem(orders
                    ,wordTypeDataForEMI(orders, new StringBuilder("3.5"), orders.get(0).getCustomerName(), orders.get(0).getTotalCost(), (int) annualIntrestRate, LoanTenure, EMI)
                    ,"docx");
            return isPaymentCompleted();
        } catch (Exception message) {
            throw new PlacedOrderException(message.getMessage());
        }

    }

    public int getPrincipal(AdsCostTabel adsCost, int days) throws JsonProcessingException {
        String adsCostUrl = "http://localhost:8082/AdsCosting-api/choose-ads?adtype=" +adsCost.getType()+ "&days=" +days;
        String AdsCost = this.restTemplate.getForObject(adsCostUrl, String.class);

       int adcost = extractNumberFromString(AdsCost);
        return adcost;
    }

    public static int extractNumberFromString(String input) {
        String regex = "[,\\.\\s]";
        String[] myArray = input.split(regex);
        String number = myArray[myArray.length - 1];
        return Integer.parseInt(number);
    }
    public static double calculateEMI(double principal, double annualInterestRate, int tenureYears) {
        double monthlyInterestRate = (annualInterestRate / 12) / 100;

        int tenureMonths = tenureYears * 12;

        double emi = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureMonths)) / (Math.pow(1 + monthlyInterestRate, tenureMonths) - 1);
        return emi;
    }

    public boolean isPaymentCompleted(){
        String paymenturl="http://localhost:8082/AdsCosting-api/is-payment/completed";
        String paymentDone= this.restTemplate.getForObject(paymenturl, String.class);
        boolean response=paymentDone.equals("true")?true:false;
        return response;
    }

}