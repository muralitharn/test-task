package customerServices.softsuave.Scheduler;
import com.fasterxml.jackson.core.JsonProcessingException;
import customerServices.softsuave.enums.paymentMode;
import customerServices.softsuave.Repository.CustomerRepository;
import customerServices.softsuave.config.PaymentDetails;
import customerServices.softsuave.cuustomerServicse.CustomerServices;
import customerServices.softsuave.newModel.Customer;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
//            if (paymentDet
//            ails.getPaymentType() == paymentMode.EMI) {
//                body = "pay your bill bro! what is this " + customerDetail.getCustomerName() + "dont do this again";
//                subject = "regards your emi bro";
//                sendEmail(customerDetail.getEmail(), subject, body, senderMail);
//            }
//        }
//
    public String sendMimeHtmlContent(String toEmail, String subject, String body ,String sender) throws MessagingException, IOException {
        Session session = Session.getDefaultInstance(System.getProperties());
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(toEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(sender));
        message.setSubject(subject);

        // Create a multi-part message (text + attachment)
        MimeMultipart multipart = new MimeMultipart();

        // Text part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("This is the email body.");

        // Attachment part
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile("path/to/file.pdf");
        attachmentPart.setFileName("document.pdf");

        // Add parts to the MIME message
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);

        // Send the email (configure SMTP details)
        Transport.send(message);
//      content html cntent
        MimeMessage htmlcontent = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(htmlcontent, true, "UTF-8");

          String htmlContent = """
                    <html>
                        <body>
                            <h1 style="color: blue;">Welcome to Our Service!</h1>
                            <p>This is a <strong>test HTML email</strong> sent from Spring Boot.</p>
                            <a href="https://example.com">Visit our website</a>
                        </body>
                    </html>
                """;

        helper.setFrom(toEmail);
        helper.setTo(sender);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);  // `true` indicates HTML

        return "message sent succesfully "; }
//    }
}
