package softsuave.placeOrder.config;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private int customerId;

    private String customerName;

    private String email;

    private String address;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate=new Date();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDate;

    private boolean isActive;

    private String password;

    private String  role;

    private int cashValut =100;

}


