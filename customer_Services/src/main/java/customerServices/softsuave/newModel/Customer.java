package customerServices.softsuave.newModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    private String address;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", insertable = true, updatable = false)
    private Date creationDate = new Date();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_date", insertable = true, updatable = true)
    private Date lastUpdatedDate;

    private boolean isActive;

    @NotNull
    private String password;

    private String role;
    @Column(name = "cash_valut")
    private int cashValut = 100;


    public Customer(int id, String password, String customerName) {
        this.password = password;
        this.customerName = customerName;
    }

    public Customer(String customerName, String email, String address, boolean isActive, String password, String role) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.address = address;
        this.isActive = isActive;
        this.password = password;
        this.role = role;
    }

    public Customer(int customerId, String customerName, String email, String address, boolean isActive, String password, String role) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.address = address;
        this.isActive = isActive;
        this.password = password;
        this.role = role;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerRequestDto {


        private int id;
        private String name;
        private String email;
        private String address;
        private Date creationDate;
        private Date lastUpdatedDate;
        private boolean isActive = true;
        private String role;
        private String password;

        public CustomerRequestDto(String name, String password, String s, boolean b, String user, String chaivalaboiblood) {
            this.name = name;
            this.password = password;
        }

        public CustomerRequestDto(int id, String name, String email, String address, boolean isActive, String password, String role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.address = address;
            this.isActive = isActive;
            this.password = password;
            this.role = role;
        }

    }
}


