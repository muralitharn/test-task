package softsuave.placeOrder.Model;

import jakarta.persistence.*;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "orders_placed", indexes = {
        @Index(name = "employee_creation_date", columnList = "creation_date")
})

public class OrdersTabel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_no")
    private UUID orderNo;

    @Column(name = "Order_status", columnDefinition = "boolean default true")
    private boolean OrderStatus;

    @NotNull
    @Column(name = "order_cancellation", columnDefinition = "boolean default false")
    private boolean orderCancellation;

    @Column(name = "customer_Id")
    private int customerId;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_ads_tabel",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "order_no"),
            inverseJoinColumns = @JoinColumn(name = "Ads_id", referencedColumnName = "ADScostId"))
    private List<AdsCostTabel> adsCost;

    @Column(name = "total_Cost")
    private int totalCost;

    @Column(name = "total_days")
    private int days;

    @Column(name = "customer_Name")
    private String customerName;

    @Column(name = "is_Updatedto_Superuser")
    private boolean isUpdatedtoSuperuser;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", insertable = true, updatable = false)
    private Date creationDate = new Date();

    public OrdersTabel(boolean OrderStatus, int customerId, int days, List<AdsCostTabel> adsCost, boolean isUpdatedtoSuperuser) {
        this.OrderStatus = OrderStatus;
        this.customerId = customerId;
        this.days = days;
        this.adsCost = adsCost;
        this.isUpdatedtoSuperuser = isUpdatedtoSuperuser;
    }

    public OrdersTabel(UUID orderNo, boolean orderStatus, boolean orderCancellation, int customerId, List<AdsCostTabel> adsCost, int totalCost, int days, String customerName, @NonNull boolean isUpdatedtoSuperuser, Date creationDate) {
        this.orderNo = orderNo;
        OrderStatus = orderStatus;
        this.orderCancellation = orderCancellation;
        this.customerId = customerId;
        this.adsCost = adsCost;
        this.totalCost = totalCost;
        this.days = days;
        this.customerName = customerName;
        this.isUpdatedtoSuperuser = isUpdatedtoSuperuser;
        this.creationDate = creationDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class orderDto {

        private boolean orderStatus;
        private int customerId;
        private int days;
        private List<String> adsCost;
        private boolean isUpdatedtoSuperuser;

    }

}

