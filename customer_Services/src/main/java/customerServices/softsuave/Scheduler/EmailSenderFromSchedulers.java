package customerServices.softsuave.Scheduler;
import com.fasterxml.jackson.core.JsonProcessingException;
import customerServices.softsuave.enums.paymentMode;
import customerServices.softsuave.Repository.CustomerRepository;
import customerServices.softsuave.config.PaymentDetails;
import customerServices.softsuave.cuustomerServicse.CustomerServices;
import customerServices.softsuave.newModel.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class EmailSenderFromSchedulers {

    @Value(value="spring.sender.mail.id")
    private String senderMail;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    CustomerRepository customerRepos;

    @Autowired
    CustomerServices customerServices;

    String subject,body;

    public void sendEmail(String toEmail, String subject, String body ,String sender) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(sender);
        mailSender.send(message);

    }
//
//    @Scheduled(fixedRate = 45000)
//    @Transactional
//    public void GreetingMessage() {
//        List<Customer> customer = customerRepos.findAll();
//
//        for (Customer itreate : customer) {
//            body = " hello this is  gretting message for " + itreate.getCustomerName() + " for the customer id " + itreate.getCustomerId() +
//                    "your are doing very great job by being  our customer  ,";
//            subject = " welcome our fellow customer !  ";
//            sendEmail(itreate.getEmail(), subject, body, senderMail);
//        }
//    }
//
//
//    @Scheduled(cron = "0 22 * * * 1-5")
//    public void remainderForEMI() throws JsonProcessingException {
//
//        List<Customer> customer = customerRepos.findAll();
//        for (Customer customerDetail : customer) {
//            PaymentDetails paymentDetails = customerServices.getpaymentModeByRest(customerDetail.getCustomerId());
//            if (paymentDetails.getPaymentType() == paymentMode.EMI) {
//                body = "pay your bill bro! what is this " + customerDetail.getCustomerName() + "dont do this again";
//                subject = "regards your emi bro";
//                sendEmail(customerDetail.getEmail(), subject, body, senderMail);
//            }
//        }
//
//    }
}
